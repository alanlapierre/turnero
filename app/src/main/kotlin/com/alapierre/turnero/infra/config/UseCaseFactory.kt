package com.alapierre.turnero.infra.config

import com.alapierre.turnero.application.ScheduleAppointmentUseCase
import com.alapierre.turnero.domain.ports.AppointmentRepository
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class UseCaseFactory {
    @Singleton
    fun scheduleAppointmentUseCase(repo: AppointmentRepository) =
        ScheduleAppointmentUseCase(repo)
}