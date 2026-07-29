package com.lillyjourney.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pregnancies",
    foreignKeys = [ForeignKey(
        entity = ProfileEntity::class,
        parentColumns = ["id"],
        childColumns = ["profileId"],
        onDelete = ForeignKey.CASCADE,
    )],
    indices = [Index("profileId")],
)
data class PregnancyEntity(
    @PrimaryKey val id: String,
    val profileId: String = "default",
    val status: String = "active",
    val lmpDate: String? = null,
    val dueDate: String? = null,
    val dueDateSource: String? = null,
    val pregnancyOrder: String = "first",
    val multiplePregnancy: Boolean = false,
    val createdDate: String? = null,
    val endDate: String? = null,
    val notes: String? = null,
    val deliveryOutcome: String? = null,
)
