package com.lillyjourney.data.repository

import com.lillyjourney.data.db.ReminderDao
import com.lillyjourney.data.db.ReminderEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderRepository @Inject constructor(private val reminderDao: ReminderDao) {
    suspend fun getPending(): List<ReminderEntity> = reminderDao.getPending()
    suspend fun getByPregnancy(pregnancyId: String): List<ReminderEntity> = reminderDao.getByPregnancy(pregnancyId)
    suspend fun save(reminder: ReminderEntity) = reminderDao.upsert(reminder)
    suspend fun delete(id: String) = reminderDao.delete(id)
    suspend fun complete(id: String, completedTime: String) = reminderDao.complete(id, "completed", completedTime)
}
