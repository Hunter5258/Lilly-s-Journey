package com.lillyjourney.data.repository

import com.lillyjourney.data.db.AppointmentDao
import com.lillyjourney.data.db.AppointmentEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppointmentRepository @Inject constructor(
    private val appointmentDao: AppointmentDao
) {
    suspend fun getByPregnancy(pregnancyId: String): List<AppointmentEntity> =
        appointmentDao.getByPregnancy(pregnancyId)

    suspend fun getUpcoming(): List<AppointmentEntity> = appointmentDao.getUpcoming()

    suspend fun save(appointment: AppointmentEntity) = appointmentDao.upsert(appointment)

    suspend fun delete(id: String) = appointmentDao.delete(id)

    suspend fun markAttended(id: String) = appointmentDao.updateStatus(id, "attended")

    suspend fun markMissed(id: String) = appointmentDao.updateStatus(id, "missed")
}
