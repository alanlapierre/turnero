plugins {
    id("io.micronaut.application")
    kotlin("kapt")
}

micronaut {
    //version("4.3.8")
    runtime("netty")
    testRuntime("kotest5")
    processing {
        incremental(true)
        annotations("com.alapierre.*")
    }
}

dependencies {
    kapt("io.micronaut.serde:micronaut-serde-processor")
    implementation(kotlin("reflect"))

    implementation(project(":application"))
    implementation(project(":domain"))

    implementation("io.micronaut:micronaut-http-server-netty")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.validation:micronaut-validation")
    implementation("jakarta.annotation:jakarta.annotation-api")

    implementation("io.micronaut.serde:micronaut-serde-jackson")

    // MongoDB (driver síncrono integrado con Micronaut)
    implementation("io.micronaut.mongodb:micronaut-mongo-sync")

    // Logging JSON (Logback + encoder)
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")

    // Tracing (para MDC traceId/spanId y OTel)
    implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp") // para traces (OTLP)

    //Micrometer para Metricas
    implementation("io.micronaut.micrometer:micronaut-micrometer-core")
    implementation("io.micronaut.micrometer:micronaut-micrometer-registry-prometheus")
    implementation("io.micronaut:micronaut-management")

    runtimeOnly("ch.qos.logback:logback-classic")

    testImplementation("io.micronaut:micronaut-http-client")
}

application {
    mainClass.set("com.alapierre.turnero.Application")
}

