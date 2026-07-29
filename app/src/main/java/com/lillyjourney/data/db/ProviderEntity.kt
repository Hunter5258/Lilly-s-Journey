package com.lillyjourney.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "providers")
data class ProviderEntity(
    @PrimaryKey val id: String,
    val name: String,
    val specialty: String? = null,
    val clinic: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val notes: String? = null,
)
