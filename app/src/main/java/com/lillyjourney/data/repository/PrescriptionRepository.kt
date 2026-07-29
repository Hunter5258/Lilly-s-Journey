package com.lillyjourney.data.repository

import com.lillyjourney.data.db.PrescriptionDao
import com.lillyjourney.data.db.PrescriptionEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrescriptionRepository @Inject constructor(private val prescriptionDao: PrescriptionDao) {
    suspend fun getByPregnancy(pregnancyId: String): List<PrescriptionEntity> = prescriptionDao.getByPregnancy(pregnancyId)
    suspend fun save(prescription: PrescriptionEntity) = prescriptionDao.upsert(prescription)
    suspend fun delete(id: String) = prescriptionDao.delete(id)
}
