rootProject.name = "kopama"

include("kopama-core")
include("kopama-ksp")
include("kopama-example")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("ksp", "com.google.devtools.ksp:symbol-processing-api:1.9.21-1.0.15")
            library("kotlinpoet-ksp", "com.squareup:kotlinpoet-ksp:1.15.2")
            library("kotlin-reflect", "org.jetbrains.kotlin:kotlin-reflect:1.9.21")
            library("kotest-junit", "io.kotest:kotest-runner-junit5-jvm:5.8.0")
            library("kotest-assertions", "io.kotest:kotest-assertions-core-jvm:5.8.0")
        }
    }
}
