package com.lillyjourney.data.repository

import com.lillyjourney.data.db.VitalDao
import com.lillyjourney.data.db.VitalEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VitalRepository @Inject constructor(private val vitalDao: VitalDao) {
    suspend fun getByPregnancy(pregnancyId: String): List<VitalEntity> = vitalDao.getByPregnancy(pregnancyId)
    suspend fun getByType(type: String, pregnancyId: String): List<VitalEntity> = vitalDao.getByType(type, pregnancyId)
    suspend fun save(vital: VitalEntity) = vitalDao.upsert(vital)
    suspend fun delete(id: String) = vitalDao.delete(id)
}
