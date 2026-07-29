package com.lillyjourney.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vaccinations",
    foreignKeys = [ForeignKey(
        entity = PregnancyEntity::class,
        parentColumns = ["id"],
        childColumns = ["pregnancyId"],
        onDelete = ForeignKey.CASCADE,
    )],
    indices = [Index("pregnancyId")],
)
data class VaccinationEntity(
    @PrimaryKey val id: String,
    val pregnancyId: String? = null,
    val name: String,
    val dueDate: String? = null,
    val administeredDate: String? = null,
    val status: String = "pending",
    val notes: String? = null,
)
