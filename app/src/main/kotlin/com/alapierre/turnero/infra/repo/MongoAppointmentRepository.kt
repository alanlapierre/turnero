package com.alapierre.turnero.infra.repo

import com.alapierre.turnero.domain.Appointment
import com.alapierre.turnero.domain.AppointmentId
import com.alapierre.turnero.domain.ClientId
import com.alapierre.turnero.domain.EmployeeId
import com.alapierre.turnero.domain.ports.AppointmentRepository
import com.mongodb.client.MongoClient
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Filters.lt
import com.mongodb.client.model.Filters.gt
import com.mongodb.client.model.ReplaceOptions
import jakarta.inject.Singleton
import org.bson.Document
import java.time.Duration
import java.time.Instant

private const val DB_NAME = "turnero"
private const val COLLECTION = "appointments"

@Singleton
class MongoAppointmentRepository(
    private val mongoClient: MongoClient
) : AppointmentRepository {

    private fun col() = mongoClient.getDatabase(DB_NAME).getCollection(COLLECTION)

    override fun save(appointment: Appointment): Appointment {
        val doc = Document(
            mapOf(
                "_id" to appointment.id.value,
                "employeeId" to appointment.employeeId.value,
                "clientId" to appointment.clientId.value,
                "start" to appointment.start.toString(),           // guardamos ISO-8601
                "durationMinutes" to appointment.duration.toMinutes()
            )
        )
        col().replaceOne(eq("_id", appointment.id.value), doc, ReplaceOptions().upsert(true))
        return appointment
    }

    override fun findById(id: AppointmentId): Appointment? {
        val d = col().find(eq("_id", id.value)).firstOrNull() ?: return null
        return toDomain(d)
    }

    override fun findByEmployeeBetween(employeeId: EmployeeId, from: Instant, to: Instant): List<Appointment> {
        val cursor = col().find(
            and(
                eq("employeeId", employeeId.value),
                lt("start", to.toString()),
                gt("start", from.toString())
            )
        )
        return cursor.map { toDomain(it) }.toList()
    }

    private fun toDomain(d: Document): Appointment =
        Appointment(
            id = AppointmentId(d.getString("_id")),
            employeeId = EmployeeId(d.getString("employeeId")),
            clientId = ClientId(d.getString("clientId")),
            start = Instant.parse(d.getString("start")),
            duration = Duration.ofMinutes(d.getLong("durationMinutes"))
        )
}