package com.lillyjourney.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PrescriptionDao {
    @Query("SELECT * FROM prescriptions WHERE pregnancyId = :pregnancyId ORDER BY date DESC")
    suspend fun getByPregnancy(pregnancyId: String): List<PrescriptionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(prescription: PrescriptionEntity)

    @Query("DELETE FROM prescriptions WHERE id = :id")
    suspend fun delete(id: String)
}
