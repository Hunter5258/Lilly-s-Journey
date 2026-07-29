package com.lillyjourney.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "symptoms",
    foreignKeys = [ForeignKey(
        entity = PregnancyEntity::class,
        parentColumns = ["id"],
        childColumns = ["pregnancyId"],
        onDelete = ForeignKey.CASCADE,
    )],
    indices = [Index("pregnancyId")],
)
data class SymptomEntity(
    @PrimaryKey val id: String,
    val pregnancyId: String? = null,
    val name: String,
    val severity: Int = 3,
    val frequency: String? = null,
    val dateTime: String? = null,
    val notes: String? = null,
    val isUrgent: Boolean = false,
)
