package com.lillyjourney.di

import android.content.Context
import androidx.room.Room
import com.lillyjourney.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "lillys_journey.db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides fun provideProfileDao(db: AppDatabase) = db.profileDao()
    @Provides fun providePregnancyDao(db: AppDatabase) = db.pregnancyDao()
    @Provides fun provideMedicineDao(db: AppDatabase) = db.medicineDao()
    @Provides fun providePrescriptionDao(db: AppDatabase) = db.prescriptionDao()
    @Provides fun provideTestDao(db: AppDatabase) = db.testDao()
    @Provides fun provideVitalDao(db: AppDatabase) = db.vitalDao()
    @Provides fun provideVaccinationDao(db: AppDatabase) = db.vaccinationDao()
    @Provides fun provideAppointmentDao(db: AppDatabase) = db.appointmentDao()
    @Provides fun provideProviderDao(db: AppDatabase) = db.providerDao()
    @Provides fun provideSymptomDao(db: AppDatabase) = db.symptomDao()
    @Provides fun provideSafetyAlertDao(db: AppDatabase) = db.safetyAlertDao()
    @Provides fun provideReminderDao(db: AppDatabase) = db.reminderDao()
    @Provides fun provideAttachmentDao(db: AppDatabase) = db.attachmentDao()
}
