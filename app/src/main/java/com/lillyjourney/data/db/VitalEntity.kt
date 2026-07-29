package com.lillyjourney.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vitals",
    foreignKeys = [ForeignKey(
        entity = PregnancyEntity::class,
        parentColumns = ["id"],
        childColumns = ["pregnancyId"],
        onDelete = ForeignKey.CASCADE,
    )],
    indices = [Index("pregnancyId")],
)
data class VitalEntity(
    @PrimaryKey val id: String,
    val pregnancyId: String? = null,
    val type: String,
    val value: Double? = null,
    val systolic: Int? = null,
    val diastolic: Int? = null,
    val dateTime: String? = null,
    val source: String = "home",
    val notes: String? = null,
)
