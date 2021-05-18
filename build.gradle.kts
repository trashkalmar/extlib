plugins {
  kotlin("jvm") version "1.5.0"
  `maven-publish`
}

repositories {
  mavenCentral()
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

publishing {
  publications {
    create<MavenPublication>("maven") {
      groupId = "com.pocketimps"
      artifactId = "extlib"
      version = "1.0.1"

      from(components["kotlin"])
    }
  }
}
