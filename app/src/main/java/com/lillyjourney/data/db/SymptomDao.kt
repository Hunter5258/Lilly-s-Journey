package com.lillyjourney.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SymptomDao {
    @Query("SELECT * FROM symptoms WHERE pregnancyId = :pregnancyId ORDER BY dateTime DESC")
    suspend fun getByPregnancy(pregnancyId: String): List<SymptomEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(symptom: SymptomEntity)

    @Query("DELETE FROM symptoms WHERE id = :id")
    suspend fun delete(id: String)
}
