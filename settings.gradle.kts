pluginManagement {
  repositories {
    maven("https://maven.fabricmc.net/") // Fabric Maven Repository
    maven("https://maven.minecraftforge.net/") // Minecraft Forge Maven Repository
    maven("https://maven.architectury.dev/") // Architectury Maven Repository
    maven("https://maven.neoforged.net/releases/") // NeoForge Maven Repository
    gradlePluginPortal() // Gradle Plugin Repository
  }
  plugins {
    kotlin("jvm") version "2.0.0"
  }
}
plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "ProjectAuroraMinecraftMod" // Sets project name

include("common") // Common directory
include("fabric") // Fabric directory
include("neoforge") // NeoForge directory
include("forge") // Forge directory

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
