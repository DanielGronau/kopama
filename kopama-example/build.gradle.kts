plugins {
    application
    kotlin("jvm")
    id("com.google.devtools.ksp") version "2.0.10-1.0.24"
}

group = "kopama-example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

kotlin.sourceSets["main"].kotlin.srcDir("$buildDir/generated/ksp/main/")

sourceSets {
    main {
        java {
            srcDir("$buildDir/generated/ksp/main")
        }
    }
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