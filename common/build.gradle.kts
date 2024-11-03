plugins {
  kotlin("jvm")
}
val platforms: String by project

architectury {
  common(platforms.split(","))
}

val shadowCommon by configurations.registering

loom {
  silentMojangMappingsLicense()
}

val archivesBaseName: String by project

dependencies {
  minecraft(libs.minecraft)
  mappings(
    loom.layered {
      officialMojangMappings()
      parchment(libs.parchment)
    },
  )

  modImplementation(libs.fabric.loader)
  implementation(kotlin("stdlib-jdk8"))
}

tasks {
  remapJar {
    archiveBaseName.set("$archivesBaseName-Common")
    archiveClassifier.set(libs.versions.minecraft)
  }
}

publishing {
  publications {
    create<MavenPublication>("common") {
      groupId = project.group.toString()
      artifactId = "project-aurora-common"

      from(components["java"])
    }
  }
}
repositories {
  mavenCentral()
}
kotlin {
  jvmToolchain(21)
}
