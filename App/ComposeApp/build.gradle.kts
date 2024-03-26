import org.apache.commons.net.ftp.FTP
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.*
import org.apache.commons.net.ftp.FTPClient
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.sqlDelight)
    id("kotlin-parcelize")
//    kotlin("jvm")
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    js(IR) {
        moduleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
//        browser()
        binaries.executable()
    }

    androidTarget {

        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"

            }

        }
    }

    jvm("desktop")


    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.sqlDelight.driver.android)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(projects.shared)

//            implementation(libs.voyager.navigator)
            implementation(libs.composeImageLoader)
            implementation(libs.napier)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.core)
            implementation(libs.ktor.client.logging)


            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)

            implementation(projects.app.feature.account)
            implementation(projects.app.feature.course)
            implementation(projects.app.feature.home)
            implementation(projects.app.feature.navigation)
            implementation(projects.app.feature.di)
            implementation(projects.app.feature.core)
            implementation(projects.app.feature.publication)
            implementation(projects.app.feature.notification)
            implementation(projects.domain)


            implementation(libs.compose.filePicker)
            implementation(libs.ktor.server.serialization.json)
            implementation(libs.ktor.client.content.negotiation)

            implementation(libs.decompose)
            implementation(libs.essenty.parcelable)
            implementation(libs.essenty.lifecycle)

            implementation(libs.decompose.compose.multiplatform)

        }


        jsMain.dependencies {
            implementation(compose.html.core)
            implementation(compose.runtime)
            implementation(libs.sqlDelight.driver.js)
            implementation(libs.ktor.client.js)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sqlDelight.driver.native)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.common)
            implementation(compose.desktop.currentOs)
            implementation(libs.org.apache.log4j.core)
            implementation(libs.org.apache.log4j.impl)
            implementation(libs.org.apache.log4j.api)
            implementation("commons-net:commons-net:3.8.0")
        }


//        jvmMain.dependencies {
//            implementation(compose.desktop.common)
//            implementation(compose.desktop.currentOs)
////            implementation(libs.ktor.client.okhttp)
//            implementation(libs.sqlDelight.driver.sqlite)
//        }

    }
}

android {

    namespace = "ru.online.education"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "ru.online.education"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    dependencies {
//        debugImplementation(libs.compose.ui.tooling)
    }
}


compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ru.online.education"
            packageVersion = "1.0.0"
        }
    }
}
//
compose.experimental {
    web.application {}
}

//sqldelight {
//    databases {
//        create("MyDatabase") {
//            // Database configuration here.
//            // https://cashapp.github.io/sqldelight
//            packageName.set("org.company.app.db")
//        }
//    }
//}


buildscript {
    dependencies {
        classpath("commons-net:commons-net:3.10.0")
    }
}

tasks.register<Jar>("publishJsApp") {
    dependsOn("jsBrowserDevelopmentExecutableDistribution")

    outputs.upToDateWhen { false }

    doLast {
        val ftpClient = FTPClient()

        val props = Properties()
        file("../../local.properties").inputStream().use { props.load(it) }

        val server = props.getProperty("ftp.server")
        val username = props.getProperty("ftp.username")
        val password = props.getProperty("ftp.password")

        ftpClient.connect(server)
        ftpClient.login(username, password)
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE)

        try {
            val ftr = fileTree("${project.projectDir}\\build\\dist\\js\\developmentExecutable\\")
            println("baseDir ${project.projectDir}\\build\\dist\\js\\developmentExecutable")

            ftr.forEach { file ->
                print("sending file ${file.name}")
                FileInputStream(file).use { fis ->
                    println(
                        if (ftpClient.storeFile(
                                "vkv34.beget.tech/public_html//${file.name}",
                                fis
                            )
                        ) " sended" else " error while sending"
                    )
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }


        println("files sended")

        ftpClient.logout()
        ftpClient.disconnect()
    }
}