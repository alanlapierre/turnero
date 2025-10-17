package com.alapierre.turnero.application


import com.alapierre.turnero.domain.Appointment
import com.alapierre.turnero.domain.AppointmentId
import com.alapierre.turnero.domain.EmployeeId
import com.alapierre.turnero.domain.ports.AppointmentRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.assertions.throwables.shouldThrow
import java.time.Instant

class ScheduleAppointmentUseCaseTest : StringSpec({
    "rechaza solapamiento" {
        val repo = object : AppointmentRepository {
            private val data = mutableListOf<Appointment>()
            override fun save(a: Appointment) = a.also { data += it }
            override fun findById(id: AppointmentId) = data.find { it.id == id }
            override fun findByEmployeeBetween(e: EmployeeId, f: Instant, t: Instant) =
                data.filter { it.employeeId == e && it.start < t && it.end > f }
        }

        val useCase = ScheduleAppointmentUseCase(repo)
        val cmd1 = ScheduleAppointmentCommand("e1","c1", Instant.parse("2025-01-01T10:00:00Z"), 30)
        useCase.handle(cmd1) // ok

        val cmd2 = ScheduleAppointmentCommand("e1","c2", Instant.parse("2025-01-01T10:15:00Z"), 30)
        shouldThrow<IllegalArgumentException> { useCase.handle(cmd2) }
    }
})