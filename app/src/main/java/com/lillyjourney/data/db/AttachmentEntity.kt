package com.lillyjourney.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attachments")
data class AttachmentEntity(
    @PrimaryKey val id: String,
    val fileName: String,
    val fileType: String,
    val size: Long = 0,
    val ownerType: String,
    val ownerId: String,
    val data: ByteArray? = null,
    val uploadDate: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AttachmentEntity) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}
