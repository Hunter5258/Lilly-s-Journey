package com.lillyjourney.data.repository

import com.lillyjourney.data.db.VaccinationDao
import com.lillyjourney.data.db.VaccinationEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VaccinationRepository @Inject constructor(private val vaccinationDao: VaccinationDao) {
    suspend fun getByPregnancy(pregnancyId: String): List<VaccinationEntity> = vaccinationDao.getByPregnancy(pregnancyId)
    suspend fun save(vaccination: VaccinationEntity) = vaccinationDao.upsert(vaccination)
    suspend fun delete(id: String) = vaccinationDao.delete(id)
}
