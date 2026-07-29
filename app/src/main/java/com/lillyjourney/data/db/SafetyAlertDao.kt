package com.lillyjourney.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SafetyAlertDao {
    @Query("SELECT * FROM safety_alerts WHERE pregnancyId = :pregnancyId ORDER BY timestamp DESC")
    suspend fun getByPregnancy(pregnancyId: String): List<SafetyAlertEntity>

    @Query("SELECT * FROM safety_alerts WHERE acknowledged = 0 ORDER BY timestamp DESC")
    suspend fun getUnacknowledged(): List<SafetyAlertEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(alert: SafetyAlertEntity)

    @Query("UPDATE safety_alerts SET acknowledged = 1 WHERE id = :id")
    suspend fun acknowledge(id: String)
}
