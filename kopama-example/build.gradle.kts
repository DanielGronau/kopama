plugins {
    application
    kotlin("jvm") version "1.9.0"
    id("com.google.devtools.ksp") version "1.9.0-1.0.11"
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