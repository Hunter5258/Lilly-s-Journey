package com.lillyjourney.data.repository

import com.lillyjourney.data.db.MedicineDao
import com.lillyjourney.data.db.MedicineEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicineRepository @Inject constructor(
    private val medicineDao: MedicineDao
) {
    suspend fun getByPregnancy(pregnancyId: String): List<MedicineEntity> =
        medicineDao.getByPregnancy(pregnancyId)

    suspend fun getActive(): List<MedicineEntity> = medicineDao.getActive()

    suspend fun save(medicine: MedicineEntity) = medicineDao.upsert(medicine)

    suspend fun delete(id: String) = medicineDao.delete(id)
}
