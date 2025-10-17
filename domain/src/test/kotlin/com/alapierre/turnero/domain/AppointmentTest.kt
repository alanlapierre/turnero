package com.alapierre.turnero.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Duration
import java.time.Instant

class AppointmentTest : StringSpec({
    "detecta solapamiento por empleado" {
        val emp = EmployeeId("e1")
        val a = Appointment(employeeId = emp, clientId = ClientId("c1"),
            start = Instant.parse("2025-01-01T10:00:00Z"), duration = Duration.ofMinutes(30))
        val b = Appointment(employeeId = emp, clientId = ClientId("c2"),
            start = Instant.parse("2025-01-01T10:15:00Z"), duration = Duration.ofMinutes(30))
        a.overlaps(b) shouldBe true
    }
})