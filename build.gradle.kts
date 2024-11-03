import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

plugins {
  alias(libs.plugins.architectury.loom) apply false
  alias(libs.plugins.architectury.plugin)

  alias(libs.plugins.detekt)
  alias(libs.plugins.shadow) apply false

  alias(libs.plugins.indra.main)
  alias(libs.plugins.spotless)
  alias(libs.plugins.unifiedpublishing) apply false
  kotlin("jvm")
}

architectury {
  minecraft = libs.versions.minecraft.toString() // Sets Minecraft version from gradle/libs.versions.toml
}

val modVersion: String by project
val mavenGroup: String by project
val archivesBaseName: String by project
val supportedMinecraftVersions: String by project
val supportedMinecraftVersionsList: String by project
val curseforgeProjectId: String by project
val modrinthProjectId: String by project

allprojects {
  apply(plugin = "java")
  apply(plugin = "architectury-plugin")
  apply(plugin = "maven-publish")
  apply(plugin = "com.diffplug.spotless")
  apply(plugin = "io.gitlab.arturbosch.detekt")
  apply(plugin = "net.kyori.indra")
  apply(plugin = "net.kyori.indra.git")

  version = modVersion
  group = mavenGroup

  ext["releaseChangelog"] = releaseChangelog()

  dependencies {
    detektPlugins(rootProject.libs.detekt.formatting)
    detektPlugins(rootProject.libs.detekt.libraries)
  }

  indra {
    mitLicense()
    javaVersions {
      target(21)
      testWith(21)
    }
  }

  detekt {
    buildUponDefaultConfig = true
    config.setFrom(rootProject.files("detekt.yml"))
  }

  spotless {
    kotlin {
      licenseHeaderFile(file(rootProject.files("license_header.txt").singleFile))
    }
    java {
      licenseHeaderFile(file(rootProject.files("license_header.txt").singleFile))
    }
  }

  java {
    withSourcesJar()
  }

  tasks.withType<JavaCompile> {
    val compilerArgs = options.compilerArgs
    compilerArgs.add("-Xlint:-processing")
  }
}

subprojects {
  apply(
    plugin =
    rootProject.libs.plugins.architectury.loom
      .get()
      .pluginId,
  )
  apply(
    plugin =
    rootProject.libs.plugins.shadow
      .get()
      .pluginId,
  )
  apply(
    plugin =
    rootProject.libs.plugins.unifiedpublishing
      .get()
      .pluginId,
  )

  repositories {
    maven("https://maven.parchmentmc.org")
  }
}

/**
 * Generates the changelog for the release.
 */
fun releaseChangelog(): String {
  val dateFormat = SimpleDateFormat("yyyy-MM-dd")
  dateFormat.timeZone = TimeZone.getTimeZone("UTC")
  val date = dateFormat.format(Date())

  val changelogFormatText = file("docs/changelogs/format.md").readText().trim()
  val changelogText = file("docs/changelogs/$version.md").readText().trim()
  val lastVersion = changelogText.split("\n")[0].split("=")[1].trim()

  val changelog =
    changelogFormatText
      .replace("%date%", date)
      .replace("%changelog%", changelogText)
      .replace("%version%", version.toString())
      .replace("%mc_versions%", supportedMinecraftVersions)
      .replace("%last_version%", lastVersion)

  val changelogBuilder = StringBuilder()
  for (line in changelog.split("\n")) {
    if (!line.startsWith("!")) {
      changelogBuilder.append(line).append("\n")
    }
  }

  if (!rootProject.layout.buildDirectory
      .get()
      .asFile
      .exists()
  ) {
    rootProject.layout.buildDirectory
      .get()
      .asFile
      .mkdirs()
  }
  File(
    rootProject.layout.buildDirectory
      .get()
      .asFile,
    "CHANGELOG.md",
  ).writeText(changelogBuilder.toString().trim())
  return changelogBuilder.toString().trim()
}

repositories {
  mavenCentral()
}
dependencies {
  implementation(kotlin("stdlib-jdk8"))
}
kotlin {
  jvmToolchain(21)
}
