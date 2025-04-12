
plugins {
    id("org.jetbrains.kotlin.jvm") version "2.1.10"
    id("org.openapi.generator") version "7.0.0"
    application
}

group = "com.example.app"
version = "0.1.0"

repositories {
    mavenCentral()
}

val koinVersion = "4.1.0-Beta5"
val ktorVersion = "3.1.2"

val ktorDependencies = listOf(
    "io.ktor:ktor-serialization-kotlinx-json",
    "io.ktor:ktor-server-auth-jwt",
    "io.ktor:ktor-server-auto-head-response",
    "io.ktor:ktor-server-call-logging",
    "io.ktor:ktor-server-cio",
    "io.ktor:ktor-server-compression",
    "io.ktor:ktor-server-content-negotiation",
    "io.ktor:ktor-server-cors",
    "io.ktor:ktor-server-default-headers",
    "io.ktor:ktor-server-hsts",
    "io.ktor:ktor-server-status-pages",
)

dependencies {
    ktorDependencies.forEach { implementation("$it:$ktorVersion") }
    implementation("io.insert-koin:koin-ktor3:$koinVersion")

    // Test dependencies.
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("io.insert-koin:koin-test-junit5:$koinVersion")
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
