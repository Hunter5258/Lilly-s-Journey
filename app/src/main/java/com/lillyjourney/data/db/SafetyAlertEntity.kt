package com.lillyjourney.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "safety_alerts",
    foreignKeys = [ForeignKey(
        entity = PregnancyEntity::class,
        parentColumns = ["id"],
        childColumns = ["pregnancyId"],
        onDelete = ForeignKey.CASCADE,
    )],
    indices = [Index("pregnancyId")],
)
data class SafetyAlertEntity(
    @PrimaryKey val id: String,
    val pregnancyId: String? = null,
    val warningType: String,
    val severity: String = "high",
    val timestamp: String? = null,
    val notes: String? = null,
    val acknowledged: Boolean = false,
    val followUpStatus: String? = null,
    val emergencyContactAction: Boolean = false,
)
