rootProject.name = "kopama"

include("kopama-core")
include("kopama-ksp")
include("kopama-example")

dependencyResolutionManagement {
    val kotlinVersion = "2.0.10"
    val kotestVersion = "5.9.1"
    versionCatalogs {
        create("libs") {
            library("ksp", "com.google.devtools.ksp:symbol-processing-api:$kotlinVersion-1.0.24")
            library("kotlin-reflect", "org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
            library("kotlinpoet-ksp", "com.squareup:kotlinpoet-ksp:1.18.1")
            library("kotest-junit", "io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
            library("kotest-assertions", "io.kotest:kotest-assertions-core-jvm:$kotestVersion")
        }
    }
}
