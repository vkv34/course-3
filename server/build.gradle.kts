plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}

group = "ru.online.education"
version = "1.0.0"
application {
    mainClass.set("ru.online.education.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["development"] ?: "false"}")
}

//sourceSets{
//    val a = project(":di").extensions.getByType(SourceSetContainer::class).getByName("ktorMain")
//
//    val main by getting{
//        compileClasspath += a.compileClasspath
//        runtimeClasspath += a.runtimeClasspath
//    }
//}

dependencies {
    implementation(projects.shared)
    implementation(projects.domain)

    implementation(libs.koin.ktor)
    implementation(libs.koin.logger)

    with(libs.exposed){
        implementation(core)
        implementation(dao)
        implementation(jdbc)
        implementation(crypt)
        implementation(kotlin.datetime)
    }
    implementation(libs.mysql.connector)


    implementation(libs.logback)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.content.negotination)
    implementation(libs.ktor.server.serialization.json)
    implementation(libs.ktor.server.validation)
    implementation(libs.ktor.server.status.pages)

    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.netty)
    testImplementation(libs.ktor.server.tests)



    implementation(libs.ktor.server.metric.micrometer)
    implementation(libs.micrometer.registry.prometheus)

    testImplementation(libs.kotlin.test.junit)
}