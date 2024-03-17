import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
//    alias(libs.plugins.buildConfig)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    js(IR) {
        browser()
    }

//    androidTarget {
//        compilations.all {
//            kotlinOptions {
//                jvmTarget = "1.8"
//            }
//        }
//    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvm("desktop")

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    sourceSets {


//         androidMain.dependencies {
////             implementation(libs.compose.ui.tooling.preview)
//             implementation(libs.androidx.activity.compose)
//             implementation(libs.kotlinx.coroutines.android)
//         }
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
            implementation(projects.shared)
//            implementation(libs.voyager.navigator)
//            implementation(libs.composeImageLoader)
//            implementation(libs.napier)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
//            implementation(libs.kotlinx.serialization.protobuf)
//            implementation(libs.androidx.datastore.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.ktor.core)


            implementation(projects.domain)
            implementation(projects.app.feature.core)

//            implementation(projects.sharedCompose)


        }


        jsMain.dependencies {
            implementation(compose.html.core)
            implementation(libs.kotlinx.html.js)
        }

//        iosMain.dependencies {
//            implementation(libs.ktor.dar)
//        }
//
    }
}
//
android {
    namespace = "ru.online.education.app.feature.account"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
//dependencies {
////    implementation(libs.androidx.ui.tooling.preview.android)
//}
