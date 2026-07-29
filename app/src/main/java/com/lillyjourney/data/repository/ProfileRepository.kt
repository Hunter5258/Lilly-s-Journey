package com.lillyjourney.data.repository

import com.lillyjourney.data.db.ProfileDao
import com.lillyjourney.data.db.ProfileEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val profileDao: ProfileDao
) {
    suspend fun getProfile(): ProfileEntity? = profileDao.getProfile()
    suspend fun saveProfile(profile: ProfileEntity) = profileDao.upsert(profile)
    suspend fun isSetupComplete(): Boolean = profileDao.getProfile()?.name?.isNotBlank() == true
}
