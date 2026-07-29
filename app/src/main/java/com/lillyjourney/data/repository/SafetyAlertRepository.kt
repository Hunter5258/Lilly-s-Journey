package com.lillyjourney.data.repository

import com.lillyjourney.data.db.SafetyAlertDao
import com.lillyjourney.data.db.SafetyAlertEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SafetyAlertRepository @Inject constructor(private val alertDao: SafetyAlertDao) {
    suspend fun getByPregnancy(pregnancyId: String): List<SafetyAlertEntity> = alertDao.getByPregnancy(pregnancyId)
    suspend fun getUnacknowledged(): List<SafetyAlertEntity> = alertDao.getUnacknowledged()
    suspend fun save(alert: SafetyAlertEntity) = alertDao.upsert(alert)
    suspend fun acknowledge(id: String) = alertDao.acknowledge(id)
}
