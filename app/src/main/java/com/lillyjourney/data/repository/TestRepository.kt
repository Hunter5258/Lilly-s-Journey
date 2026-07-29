package com.lillyjourney.data.repository

import com.lillyjourney.data.db.TestDao
import com.lillyjourney.data.db.TestEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestRepository @Inject constructor(private val testDao: TestDao) {
    suspend fun getByPregnancy(pregnancyId: String): List<TestEntity> = testDao.getByPregnancy(pregnancyId)
    suspend fun getTrend(name: String, pregnancyId: String): List<TestEntity> = testDao.getByNameAndPregnancy(name, pregnancyId)
    suspend fun save(test: TestEntity) = testDao.upsert(test)
    suspend fun delete(id: String) = testDao.delete(id)
}
