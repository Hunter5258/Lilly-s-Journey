package com.lillyjourney.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "medicines",
    foreignKeys = [ForeignKey(
        entity = PregnancyEntity::class,
        parentColumns = ["id"],
        childColumns = ["pregnancyId"],
        onDelete = ForeignKey.CASCADE,
    )],
    indices = [Index("pregnancyId")],
)
data class MedicineEntity(
    @PrimaryKey val id: String,
    val pregnancyId: String? = null,
    val name: String,
    val dosage: String? = null,
    val strength: String? = null,
    val frequency: String? = null,
    val times: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val route: String? = null,
    val notes: String? = null,
    val status: String = "active",
    val prescriptionId: String? = null,
)
