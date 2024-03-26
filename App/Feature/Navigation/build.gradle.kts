import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    js(IR) {
        browser()
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm()
    sourceSets {


        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(projects.shared)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.ktor.core)


            implementation(projects.domain)
            implementation(projects.app.feature.core)
            implementation(projects.app.feature.home)
            implementation(projects.app.feature.account)
            implementation(projects.app.feature.course)
            implementation(projects.app.feature.notification)


            implementation(libs.napier)
            implementation(libs.decompose)
            implementation(libs.decompose.compose.multiplatform)
            implementation(libs.essenty.lifecycle)
            implementation(libs.koin.core)
            

        }


        jsMain.dependencies {
            implementation(compose.html.core)
            implementation(libs.kotlinx.html.js)
        }

    }
}