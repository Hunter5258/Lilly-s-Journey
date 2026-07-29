package com.lillyjourney.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VitalDao {
    @Query("SELECT * FROM vitals WHERE pregnancyId = :pregnancyId ORDER BY dateTime DESC")
    suspend fun getByPregnancy(pregnancyId: String): List<VitalEntity>

    @Query("SELECT * FROM vitals WHERE type = :type AND pregnancyId = :pregnancyId ORDER BY dateTime ASC")
    suspend fun getByType(type: String, pregnancyId: String): List<VitalEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(vital: VitalEntity)

    @Query("DELETE FROM vitals WHERE id = :id")
    suspend fun delete(id: String)
}
