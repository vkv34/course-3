import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.androidLibrary)
}
//dependencies {
//    implementation(libs.androidx.material3.android)
//}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    js {
        browser()
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvm()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }


    sourceSets {

        androidMain.dependencies {
            implementation(libs.androidx.material3.android)
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
            //            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.ktor.core)
            implementation(projects.domain)
            implementation(projects.app.feature.core)
            implementation(projects.shared)
            implementation(libs.composeImageLoader)
            implementation(libs.paging)
            implementation(libs.paging.compose)
            implementation(projects.app.feature.notification)


        }


    }
}

android {
    namespace = "ru.online.education.app.feature.courseCategory"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}