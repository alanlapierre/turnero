plugins {
    // Declaramos versiones acá, pero NO se aplican en el root
    kotlin("jvm") version "1.9.24" apply false
    kotlin("kapt") version "1.9.24" apply false
    id("io.micronaut.application") version "4.3.3" apply false
}

allprojects {
    repositories { mavenCentral() }
}

subprojects {
    // 1) Aplicá el plugin de Kotlin a CADA submódulo
    apply(plugin = "org.jetbrains.kotlin.jvm")

    // 2) Configurá el toolchain de manera “segura” (sin usar el helper kotlin{} que te fallaba)
    extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension> {
        jvmToolchain(21)
    }

    // 3) Dependencias de test (usando strings para evitar el helper kotlin("test"))
    dependencies {
        add("testImplementation", "org.jetbrains.kotlin:kotlin-test")
        add("testImplementation", "io.kotest:kotest-runner-junit5:5.9.1")
        add("testImplementation", "io.kotest:kotest-assertions-core:5.9.1")

    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}



