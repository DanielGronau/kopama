rootProject.name = "kopama"

include("kopama-core")
include("kopama-ksp")
include("kopama-example")

dependencyResolutionManagement {
    val kotlinVersion = "2.3.10"
    val kspVersion = "2.3.6"
    val kotestVersion = "6.1.3"
    val kotlinpoetVersion = "2.2.0"
    versionCatalogs {
        create("libs") {
            library("ksp", "com.google.devtools.ksp:symbol-processing-api:$kspVersion")
            library("kotlin-reflect", "org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
            library("kotlinpoet-ksp", "com.squareup:kotlinpoet-ksp:$kotlinpoetVersion")
            library("kotest-junit", "io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
            library("kotest-assertions", "io.kotest:kotest-assertions-core-jvm:$kotestVersion")
        }
    }
}
