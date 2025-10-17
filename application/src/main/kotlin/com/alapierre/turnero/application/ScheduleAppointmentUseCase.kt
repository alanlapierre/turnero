package com.alapierre.turnero.application

import com.alapierre.turnero.domain.Appointment
import com.alapierre.turnero.domain.ClientId
import com.alapierre.turnero.domain.EmployeeId
import com.alapierre.turnero.domain.ports.AppointmentRepository
import java.time.Instant
import java.time.Duration

data class ScheduleAppointmentCommand(
    val employeeId: String,
    val clientId: String,
    val start: Instant,
    val durationMinutes: Long
)

data class ScheduleAppointmentResult(val id: String)

class ScheduleAppointmentUseCase(
    private val repo: AppointmentRepository
) {
    fun handle(cmd: ScheduleAppointmentCommand): ScheduleAppointmentResult {
        val appointment = Appointment(
            employeeId = EmployeeId(cmd.employeeId),
            clientId = ClientId(cmd.clientId),
            start = cmd.start,
            duration = Duration.ofMinutes(cmd.durationMinutes)
        )
        // Regla: no permitir solapamiento por empleado
        val existingAppointments = repo.findByEmployeeBetween(
            appointment.employeeId,
            appointment.start.minus(Duration.ofHours(1)), // ventana simplificada
            appointment.end.plus(Duration.ofHours(1))
        )
        require(existingAppointments.none { it.overlaps(appointment) }) {
            "Appointment overlaps with an existing one"
        }
        val saved = repo.save(appointment)
        return ScheduleAppointmentResult(saved.id.value)
    }
}