package com.lillyjourney.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AppointmentDao {
    @Query("SELECT * FROM appointments WHERE pregnancyId = :pregnancyId ORDER BY dateTime ASC")
    suspend fun getByPregnancy(pregnancyId: String): List<AppointmentEntity>

    @Query("SELECT * FROM appointments WHERE status = 'upcoming' ORDER BY dateTime ASC")
    suspend fun getUpcoming(): List<AppointmentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(appointment: AppointmentEntity)

    @Query("DELETE FROM appointments WHERE id = :id")
    suspend fun delete(id: String)

    @Query("UPDATE appointments SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: String)
}
