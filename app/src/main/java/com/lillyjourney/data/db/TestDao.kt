package com.lillyjourney.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TestDao {
    @Query("SELECT * FROM tests WHERE pregnancyId = :pregnancyId ORDER BY date DESC")
    suspend fun getByPregnancy(pregnancyId: String): List<TestEntity>

    @Query("SELECT * FROM tests WHERE name = :name AND pregnancyId = :pregnancyId ORDER BY date ASC")
    suspend fun getByNameAndPregnancy(name: String, pregnancyId: String): List<TestEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(test: TestEntity)

    @Query("DELETE FROM tests WHERE id = :id")
    suspend fun delete(id: String)
}
