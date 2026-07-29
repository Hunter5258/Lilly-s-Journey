package com.lillyjourney.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tests",
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
data class TestEntity(
    @PrimaryKey val id: String,
    val pregnancyId: String? = null,
    val name: String,
    val date: String? = null,
    val providerId: String? = null,
    val result: String? = null,
    val unit: String? = null,
    val referenceRange: String? = null,
    val flag: String? = null,
    val notes: String? = null,
    val attachmentRef: String? = null,
)
