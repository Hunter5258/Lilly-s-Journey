package com.lillyjourney.data.repository

import com.lillyjourney.data.db.ProviderDao
import com.lillyjourney.data.db.ProviderEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProviderRepository @Inject constructor(
    private val providerDao: ProviderDao
) {
    suspend fun getAll(): List<ProviderEntity> = providerDao.getAll()

    suspend fun save(provider: ProviderEntity) = providerDao.upsert(provider)

    suspend fun delete(id: String) = providerDao.delete(id)
}
