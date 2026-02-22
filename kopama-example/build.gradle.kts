plugins {
    application
    kotlin("jvm")
    id("com.google.devtools.ksp") version "2.3.6"
}

group = "kopama-example"
version = "1.0-RC2"

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(project(":kopama-core"))
    ksp(project(":kopama-ksp"))
    testImplementation(kotlin("test"))
    testImplementation(libs.kotest.junit)
    testImplementation(libs.kotest.assertions)
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(20)
}