plugins {
    kotlin("jvm") version "1.9.0"
}

group = "kopama-ksp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(project(":kopama-core"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.0-1.0.11")
    implementation("com.squareup:kotlinpoet-ksp:1.14.2")

    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.4.2")
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.4.2")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(20)
}