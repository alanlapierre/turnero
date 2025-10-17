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
    implementation(project(":application"))
    implementation(project(":domain"))

    implementation("io.micronaut:micronaut-http-server-netty")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.validation:micronaut-validation")
    implementation("jakarta.annotation:jakarta.annotation-api")

    implementation("io.micronaut.serde:micronaut-serde-jackson")
    kapt("io.micronaut.serde:micronaut-serde-processor")

    // MongoDB (driver síncrono integrado con Micronaut)
    implementation("io.micronaut.mongodb:micronaut-mongo-sync")

    // (opcional pero útil)
    runtimeOnly("ch.qos.logback:logback-classic")

    testImplementation("io.micronaut:micronaut-http-client")
}

application {
    mainClass.set("com.alapierre.turnero.Application")
}

