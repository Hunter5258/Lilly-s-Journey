package com.lillyjourney.data.repository

import com.lillyjourney.data.db.SymptomDao
import com.lillyjourney.data.db.SymptomEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SymptomRepository @Inject constructor(private val symptomDao: SymptomDao) {
    suspend fun getByPregnancy(pregnancyId: String): List<SymptomEntity> = symptomDao.getByPregnancy(pregnancyId)
    suspend fun save(symptom: SymptomEntity) = symptomDao.upsert(symptom)
    suspend fun delete(id: String) = symptomDao.delete(id)
}
