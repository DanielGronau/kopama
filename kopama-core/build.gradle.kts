plugins {
    kotlin("jvm")
}

group = "kopama-core"
version = "1.0-RC2"

repositories {
    mavenCentral()
}

dependencies {
    runtimeOnly(libs.kotlin.reflect)

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