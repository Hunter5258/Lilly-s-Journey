package com.lillyjourney.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "reminders",
    foreignKeys = [ForeignKey(
        entity = PregnancyEntity::class,
        parentColumns = ["id"],
        childColumns = ["pregnancyId"],
        onDelete = ForeignKey.CASCADE,
    )],
)
data class ReminderEntity(
    @PrimaryKey val id: String,
    val pregnancyId: String? = null,
    val type: String,
    val linkedRecordType: String? = null,
    val linkedRecordId: String? = null,
    val scheduledTime: String? = null,
    val recurrence: String? = null,
    val snoozeInterval: Int = 5,
    val status: String = "pending",
    val completedTime: String? = null,
)
