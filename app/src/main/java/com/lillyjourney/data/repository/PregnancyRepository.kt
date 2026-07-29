package com.lillyjourney.data.repository

import com.lillyjourney.data.db.PregnancyDao
import com.lillyjourney.data.db.PregnancyEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PregnancyRepository @Inject constructor(
    private val pregnancyDao: PregnancyDao
) {
    suspend fun getActive(): PregnancyEntity? = pregnancyDao.getActivePregnancy()
    suspend fun getAll(): List<PregnancyEntity> = pregnancyDao.getAllPregnancies()
    suspend fun save(pregnancy: PregnancyEntity) = pregnancyDao.upsert(pregnancy)
    suspend fun archive(id: String) = pregnancyDao.updateStatus(id, "archived", null)
    suspend fun close(id: String, endDate: String) = pregnancyDao.updateStatus(id, "ended", endDate)
}
