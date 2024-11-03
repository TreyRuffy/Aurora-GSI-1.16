plugins {
  kotlin("jvm")
}
architectury {
  platformSetupLoomIde()
  forge()
}

loom {
  silentMojangMappingsLicense()

  forge {
    mixinConfig("project-aurora-common.mixins.json")
  }
}

val archivesBaseName: String by project

val common by configurations.registering
val shadowCommon by configurations.registering
configurations.compileClasspath.get().extendsFrom(common.get())
configurations["developmentForge"].extendsFrom(common.get())

repositories {
  mavenLocal()
  mavenCentral()
}

dependencies {
  minecraft(libs.minecraft)
  mappings(
    loom.layered {
      officialMojangMappings()
      parchment(libs.parchment)
    },
  )

  forge(libs.minecraftforge)

  "common"(project(path = ":common", configuration = "namedElements")) { isTransitive = false }
  "shadowCommon"(project(path = ":common", configuration = "transformProductionForge")) { isTransitive = false }
  implementation(kotlin("stdlib-jdk8"))
}

tasks {
  processResources {
    inputs.property("version", project.version)

    filesMatching("META-INF/mods.toml") {
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
    archiveBaseName.set("$archivesBaseName-MinecraftForge")
    archiveClassifier.set(libs.versions.minecraft)
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
      artifactId = "project-aurora-minecraftforge"

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
    displayName.set("Project Aurora Minecraft Mod Forge v${project.version} ($supportedMinecraftVersions)")
    version.set(project.version.toString())
    changelog.set(releaseChangelog)
    val mcVersions: List<String> = supportedMinecraftVersionsList.split(",")
    gameVersions.set(mcVersions)
    gameLoaders.set(listOf("forge"))

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
