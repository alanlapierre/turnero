package com.alapierre.turnero.infra.http

import com.alapierre.turnero.application.ScheduleAppointmentCommand
import com.alapierre.turnero.application.ScheduleAppointmentResult
import com.alapierre.turnero.application.ScheduleAppointmentUseCase
import com.alapierre.turnero.domain.AppointmentId
import com.alapierre.turnero.domain.ports.AppointmentRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Instant

@Serdeable
data class ScheduleAppointmentRequest(
    @field:NotBlank val employeeId: String,
    @field:NotBlank val clientId: String,
    val start: String, // ISO-8601
    val durationMinutes: Long
)

@Serdeable
data class CreateAppointmentResponse(val id: String)

@Serdeable
data class AppointmentResponse(val id: String, val employeeId: String, val clientId: String,
                               val start: String, val durationMinutes: Long)


@Controller("/appointments")
class AppointmentController(

    private val logger: Logger = LoggerFactory.getLogger(AppointmentController::class.java),

    private val schedule: ScheduleAppointmentUseCase,
    private val repo: AppointmentRepository
) {
    @Post
    fun create(@Body req: ScheduleAppointmentRequest): HttpResponse<CreateAppointmentResponse> {

        logger.info("Iniciando Create")

        val cmd = ScheduleAppointmentCommand(
            employeeId = req.employeeId,
            clientId = req.clientId,
            start = Instant.parse(req.start),
            durationMinutes = req.durationMinutes
        )
        val result = schedule.handle(cmd)
        return HttpResponse.created(CreateAppointmentResponse(result.id))
    }

    @Get("/{id}")
    fun get(@PathVariable id: String): HttpResponse<AppointmentResponse> {

        logger.info("Iniciando Get By Id")

        return repo.findById(AppointmentId(id))?.let {
            HttpResponse.ok(
                AppointmentResponse(
                    id = it.id.value,
                    employeeId = it.employeeId.value,
                    clientId = it.clientId.value,
                    start = it.start.toString(),
                    durationMinutes = it.duration.seconds / 60
                )
            )
        } ?: HttpResponse.notFound()

    }
}