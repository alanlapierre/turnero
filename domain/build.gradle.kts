plugins {
    // El plugin de Kotlin JVM ya se aplica desde el root (subprojects)
}
dependencies {
    // dominio puro (sin Micronaut)
    testImplementation("io.kotest:kotest-property:5.9.1")
}
