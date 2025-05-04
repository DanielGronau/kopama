plugins {
    kotlin("jvm") version "2.1.20" apply false
    id("org.jetbrains.dokka") version "2.0.0"
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
}

repositories {
    mavenCentral()
}