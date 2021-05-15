repositories {
  mavenCentral()
  maven(url = "https://jitpack.io")
}

plugins {
  kotlin("jvm") version "1.5.0"
  `maven-publish`
}

group = "com.github.trashkalmar"
version = "1.0.0"

repositories {
  maven("https://jitpack.io")
}

dependencies {
  implementation(kotlin("stdlib"))
}


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
  kotlinOptions {
    jvmTarget = "1.8"
    freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
  }
}
