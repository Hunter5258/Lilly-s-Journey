package com.lillyjourney.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminders WHERE status = 'pending' ORDER BY scheduledTime ASC")
    suspend fun getPending(): List<ReminderEntity>

    @Query("SELECT * FROM reminders WHERE pregnancyId = :pregnancyId ORDER BY scheduledTime DESC")
    suspend fun getByPregnancy(pregnancyId: String): List<ReminderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(reminder: ReminderEntity)

    @Query("DELETE FROM reminders WHERE id = :id")
    suspend fun delete(id: String)

    @Query("UPDATE reminders SET status = :status, completedTime = :completedTime WHERE id = :id")
    suspend fun complete(id: String, status: String, completedTime: String?)
}
