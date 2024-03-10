
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    //    alias(libs.plugins.androidLibrary)
//    alias(libs.plugins.jetbrainsCompose)
    //    alias(libs.plugins.buildConfig)
//    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
//    @OptIn(ExperimentalWasmDsl::class)
    js {
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

    jvm()

    sourceSets {


        //         androidMain.dependencies {
        ////             implementation(libs.compose.ui.tooling.preview)
        //             implementation(libs.androidx.activity.compose)
        //             implementation(libs.kotlinx.coroutines.android)
        //         }
        commonMain.dependencies {

        }
        jvmMain.dependencies{
            val lwjglVersion = "3.3.1"
            listOf("lwjgl", "lwjgl-tinyfd").forEach { lwjglDep ->
                implementation("org.lwjgl:${lwjglDep}:${lwjglVersion}")
                listOf(
                    "natives-windows",
                    "natives-windows-x86",
                    "natives-windows-arm64",
                    "natives-macos",
                    "natives-macos-arm64",
                    "natives-linux",
                    "natives-linux-arm64",
                    "natives-linux-arm32"
                ).forEach { native ->
                    runtimeOnly("org.lwjgl:${lwjglDep}:${lwjglVersion}:${native}")
                }
            }
        }
        
    }
}