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
            //            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(projects.shared)
            implementation(projects.app.feature.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)

            implementation(libs.ktor.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.server.serialization.json)
            implementation(libs.ktor.client.logging)


            implementation(libs.koin.core)

            implementation(projects.domain)
            implementation(projects.app.feature.account)
            implementation(projects.app.feature.course)
            implementation(projects.app.feature.publication)
            implementation(projects.app.feature.notification)
            implementation(projects.app.feature.users)

            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.coroutines)

        }

//        jvmMain.dependencies {
//            implementation(compose.desktop.common)
//            implementation(compose.desktop.currentOs)
//        }


    }
}