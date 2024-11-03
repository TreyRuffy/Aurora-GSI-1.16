plugins {
  kotlin("jvm")
}
architectury {
  platformSetupLoomIde()
  neoForge()
}

loom {
  silentMojangMappingsLicense()
  neoForge {}
}

val archivesBaseName: String by project

val common by configurations.registering
val shadowCommon by configurations.registering
configurations.compileClasspath.get().extendsFrom(common.get())
configurations["developmentNeoForge"].extendsFrom(common.get())

repositories {
  maven("https://maven.neoforged.net/releases/")
  mavenCentral()
}

dependencies {
  modules {
    // https://github.com/google/guava/issues/6618 - Fix duplicate classes error
    module("com.google.guava:listenablefuture") {
      replacedBy("com.google.guava:guava", "listenablefuture is part of guava")
    }
  }
  minecraft(libs.minecraft)
  mappings(
    loom.layered {
      officialMojangMappings()
      parchment(libs.parchment)
    },
  )

  neoForge(libs.neoforge) {
    exclude(group = "com.google.guava", module = "listenablefuture")
  }

  "common"(project(path = ":common", configuration = "namedElements")) { isTransitive = false }
  "shadowCommon"(project(path = ":common", configuration = "transformProductionForge")) { isTransitive = false }
  implementation(kotlin("stdlib-jdk8"))
}

tasks {
  processResources {
    inputs.property("version", project.version)

    filesMatching("META-INF/neoforge.mods.toml") {
      expand(
        "version" to project.version,
      )
    }
  }

  shadowJar {
    exclude("fabric.mod.json")

    configurations = listOf(shadowCommon.get())
    archiveClassifier.set("dev-shadow")
  }

  remapJar {
    inputFile.set(shadowJar.get().archiveFile)
    dependsOn(shadowJar)
    archiveBaseName.set("$archivesBaseName-NeoForge")
    archiveClassifier.set(libs.versions.minecraft)

    from(rootProject.files("LICENSE"))
  }

  jar {
    archiveClassifier.set("dev")
  }

  sourcesJar {
    val commonSources = project(":common").tasks.named<Jar>("sourcesJar")
    dependsOn(commonSources)
    from(commonSources.get().archiveFile.map { zipTree(it) })
  }
}

components["java"].withGroovyBuilder {
  "withVariantsFromConfiguration"(configurations["shadowRuntimeElements"]) {
    "skip"()
  }
}

publishing {
  publications {
    create<MavenPublication>("mavenForge") {
      groupId = project.group.toString()
      artifactId = "project-aurora-neoforge"

      from(components["java"])
    }
  }
}

val supportedMinecraftVersions: String by project
val supportedMinecraftVersionsList: String by project
val curseforgeProjectId: String by project
val modrinthProjectId: String by project
val releaseChangelog: String by ext

unifiedPublishing {
  project {
    displayName.set("Project Aurora Minecraft Mod NeoForge v${project.version} ($supportedMinecraftVersions)")
    version.set(project.version.toString())
    changelog.set(releaseChangelog)
    val mcVersions: List<String> = supportedMinecraftVersionsList.split(",")
    gameVersions.set(mcVersions)
    gameLoaders.set(listOf("neoforge"))

    val release = project.findProperty("RELEASE_TYPE") ?: System.getenv("RELEASE_TYPE")
    releaseType.set((release ?: "release").toString())

    mainPublication.set(tasks["remapJar"].outputs.files.singleFile)

    val curseApiKey = project.findProperty("CURSE_API_KEY") ?: System.getenv("CURSE_API_KEY")
    if (curseApiKey != null) {
      curseforge {
        token.set(curseApiKey.toString())
        id.set(curseforgeProjectId)
        gameVersions.addAll("Java 18", "Java 17")
        releaseType.set("beta")
      }
    }

    val modrinthToken = project.findProperty("MODRINTH_TOKEN") ?: System.getenv("MODRINTH_TOKEN")
    if (modrinthToken != null) {
      modrinth {
        token.set(modrinthToken.toString())
        id.set(modrinthProjectId)
        releaseType.set("release")
      }
    }
  }
}
kotlin {
  jvmToolchain(21)
}
