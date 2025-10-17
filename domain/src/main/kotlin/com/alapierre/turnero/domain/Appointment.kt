package com.alapierre.turnero.domain

import java.time.Duration
import java.time.Instant
import java.util.*

@JvmInline value class AppointmentId(val value: String = UUID.randomUUID().toString())
@JvmInline value class EmployeeId(val value: String)
@JvmInline value class ClientId(val value: String)

data class Appointment(
    val id: AppointmentId = AppointmentId(),
    val employeeId: EmployeeId,
    val clientId: ClientId,
    val start: Instant,
    val duration: Duration
) {
    val end: Instant get() = start.plus(duration)
    fun overlaps(other: Appointment): Boolean =
        this.employeeId == other.employeeId &&
                this.start < other.end && other.start < this.end
}
