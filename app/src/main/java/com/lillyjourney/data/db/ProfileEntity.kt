package com.lillyjourney.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey val id: String = "default",
    val name: String = "",
    val dueDate: String? = null,
    val lmpDate: String? = null,
    val dueDateSource: String? = null,
    val firstPregnancy: Boolean = true,
    val multiplePregnancy: Boolean = false,
    val language: String = "en",
    val lockType: String = "none",
    val emergencyContact: String? = null,
    val emergencyPhone: String? = null,
    val consentAcknowledged: Boolean = false,
)
