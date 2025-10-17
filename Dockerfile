# ========= Build stage (compila con Gradle wrapper) =========
FROM eclipse-temurin:21-jdk AS build
WORKDIR /work

# 1) Copiamos wrapper y configs primero para aprovechar cache de dependencias
COPY gradlew gradlew
COPY gradle gradle
RUN chmod +x gradlew

COPY gradle.properties gradle.properties

# 2) Copiamos scripts de build (si cambian, invalida el cache de dependencias)
COPY settings.gradle.kts settings.gradle.kts
COPY build.gradle.kts build.gradle.kts
COPY app/build.gradle.kts app/build.gradle.kts
COPY application/build.gradle.kts application/build.gradle.kts
COPY domain/build.gradle.kts domain/build.gradle.kts

# 3) "Calentamos" dependencias sin fuentes (si falla, que no rompa la cache)
RUN ./gradlew :app:dependencies --configuration runtimeClasspath || true

# 4) Ahora copiamos el código fuente
COPY app app
COPY application application
COPY domain domain

# 5) Construye la distribución (bin/ + lib/)
RUN ./gradlew --no-daemon --stacktrace --info :app:installDist


# ========= Run stage (imagen final liviana) =========
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiamos solo la distribución del módulo app
COPY --from=build /work/app/build/install/app /app

# Tuning de memoria (opcional)
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:+UseSerialGC"

# Puerto
EXPOSE 8080

# Ejecutar el script generado por Gradle (usa el classpath correcto)
ENTRYPOINT ["sh", "-c", "./bin/app"]

