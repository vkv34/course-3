rootProject.name = "OnlineEducation"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://jitpack.io")
        maven("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
    }
}




//include(":composeApp")
include(":server")
include(":shared")
include(":domain")
include(":App:ComposeApp")
include(":App")
include(":App:Feature")
include(":App:Feature:Core")
include(":App:Feature:Account")
include(":App:Feature:Course")
include(":App:Feature:Publication")
include(":App:Feature:FilePicker")
include(":App:Feature:Home")
include(":App:Feature:Navigation")
include(":App:Feature:Di")
include(":App:Feature:Notification")
include(":App:Feature:Users")
include(":Tasks")
//include(":App:Util")

