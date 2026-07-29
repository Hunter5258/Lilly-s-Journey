package com.lillyjourney.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PregnancyDao {
    @Query("SELECT * FROM pregnancies WHERE status = 'active' LIMIT 1")
    suspend fun getActivePregnancy(): PregnancyEntity?

    @Query("SELECT * FROM pregnancies ORDER BY createdDate DESC")
    suspend fun getAllPregnancies(): List<PregnancyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(pregnancy: PregnancyEntity)

    @Query("UPDATE pregnancies SET status = :status, endDate = :endDate WHERE id = :id")
    suspend fun updateStatus(id: String, status: String, endDate: String?)
}
