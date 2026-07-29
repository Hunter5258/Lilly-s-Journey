package com.lillyjourney.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "appointments",
    foreignKeys = [
        ForeignKey(
            entity = PregnancyEntity::class,
            parentColumns = ["id"],
            childColumns = ["pregnancyId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = ProviderEntity::class,
            parentColumns = ["id"],
            childColumns = ["providerId"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
    indices = [Index("pregnancyId"), Index("providerId")],
)
data class AppointmentEntity(
    @PrimaryKey val id: String,
    val pregnancyId: String? = null,
    val providerId: String? = null,
    val dateTime: String? = null,
    val location: String? = null,
    val purpose: String? = null,
    val reminderLeadMinutes: Int = 60,
    val status: String = "upcoming",
    val notes: String? = null,
)
