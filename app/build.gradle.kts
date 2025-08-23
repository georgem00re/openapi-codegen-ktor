
plugins {
    id("org.jetbrains.kotlin.jvm") version libs.versions.kotlin.get()
    kotlin("plugin.serialization") version libs.versions.kotlin.get()
    id("org.openapi.generator") version "7.0.0"
    application
}

group = "com.example.app"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.auto.head)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.compression)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.default.headers)
    implementation(libs.ktor.server.hsts)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.koin.ktor)
    implementation(libs.logback.classic)

    testImplementation(libs.mockk)
    testImplementation(libs.koin.test)
}

tasks.test {
    useJUnitPlatform()
}

application {
    val entryPoint = "io.ktor.server.cio.EngineMain"
    mainClass.set(entryPoint)
}

val openApiSpec = "$rootDir/openapi_spec.yml"

openApiValidate {
    inputSpec.set(openApiSpec)
}

openApiGenerate {
    inputSpec.set(openApiSpec)
    generatorName.set("kotlin-server")
    outputDir.set("$rootDir/app")
    packageName.set("com.example.app.generated")
    configOptions.set(
        mapOf(
            "featureLocations" to "false",
            "featureCORS" to "false",
            "enumPropertyNaming" to "original",
            "serializableModel" to "false",
            "featureMetrics" to "true",
            "featureResources" to "false",
        )
    )
    templateDir.set("$rootDir/app/templates")
}

listOf(
    "processResources",
    "processTestResources",
    "compileKotlin",
    "compileTestKotlin",
).forEach { name ->
    tasks.named(name).configure {
        dependsOn.add("openApiGenerate")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<JavaExec>().all {
    environment = env.allVariables()
}
