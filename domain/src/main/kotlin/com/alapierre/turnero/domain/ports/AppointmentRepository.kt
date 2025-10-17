package com.alapierre.turnero.domain.ports

import com.alapierre.turnero.domain.Appointment
import com.alapierre.turnero.domain.AppointmentId
import com.alapierre.turnero.domain.EmployeeId
import java.time.Instant

interface AppointmentRepository {
    fun save(appointment: Appointment): Appointment
    fun findById(id: AppointmentId): Appointment?
    fun findByEmployeeBetween(employeeId: EmployeeId, from: Instant, to: Instant): List<Appointment>
}