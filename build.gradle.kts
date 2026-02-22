plugins {
    kotlin("jvm") version "2.3.10" apply false
    id("org.jetbrains.dokka") version "2.1.0"
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
}

repositories {
    mavenCentral()
}