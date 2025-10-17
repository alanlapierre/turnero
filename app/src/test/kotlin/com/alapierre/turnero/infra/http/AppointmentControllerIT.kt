package com.alapierre.turnero.infra.http

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import jakarta.inject.Inject
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.micronaut.http.MediaType.APPLICATION_JSON

@MicronautTest
class AppointmentControllerIT(
    @Client("/") @Inject private val client: HttpClient
) : StringSpec({

    "crea y obtiene turno" {
        val body = """
            {"employeeId":"e1","clientId":"c1",
             "start":"2025-01-01T12:00:00Z","durationMinutes":30}
        """.trimIndent()

        val createReq = HttpRequest.POST("/appointments", body)
            .contentType(APPLICATION_JSON)
        val created = client.toBlocking().exchange(createReq, Map::class.java)

        created.status.code shouldBe 201
        val id = (created.body()["id"] as String)

        val getResp = client.toBlocking().exchange("/appointments/$id", Map::class.java)
        getResp.status.code shouldBe 200
        (getResp.body()["id"] as String) shouldBe id
    }
})