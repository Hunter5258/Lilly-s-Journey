package com.lillyjourney.data.repository

import com.lillyjourney.data.db.AttachmentDao
import com.lillyjourney.data.db.AttachmentEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AttachmentRepository @Inject constructor(private val attachmentDao: AttachmentDao) {
    suspend fun getByOwner(ownerId: String, ownerType: String): List<AttachmentEntity> =
        attachmentDao.getByOwner(ownerId, ownerType)

    suspend fun save(attachment: AttachmentEntity) = attachmentDao.upsert(attachment)

    suspend fun delete(id: String) = attachmentDao.delete(id)
}
