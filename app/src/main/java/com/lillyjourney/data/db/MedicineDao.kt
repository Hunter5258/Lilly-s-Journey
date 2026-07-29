package com.lillyjourney.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MedicineDao {
    @Query("SELECT * FROM medicines WHERE pregnancyId = :pregnancyId ORDER BY startDate DESC")
    suspend fun getByPregnancy(pregnancyId: String): List<MedicineEntity>

    @Query("SELECT * FROM medicines WHERE status = 'active' ORDER BY startDate DESC")
    suspend fun getActive(): List<MedicineEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(medicine: MedicineEntity)

    @Query("DELETE FROM medicines WHERE id = :id")
    suspend fun delete(id: String)
}
