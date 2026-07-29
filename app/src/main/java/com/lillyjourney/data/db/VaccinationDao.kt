package com.lillyjourney.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VaccinationDao {
    @Query("SELECT * FROM vaccinations WHERE pregnancyId = :pregnancyId ORDER BY dueDate ASC")
    suspend fun getByPregnancy(pregnancyId: String): List<VaccinationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(vaccination: VaccinationEntity)

    @Query("DELETE FROM vaccinations WHERE id = :id")
    suspend fun delete(id: String)
}
