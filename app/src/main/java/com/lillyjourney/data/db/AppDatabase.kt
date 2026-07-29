package com.lillyjourney.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        ProfileEntity::class,
        PregnancyEntity::class,
        MedicineEntity::class,
        PrescriptionEntity::class,
        TestEntity::class,
        VitalEntity::class,
        VaccinationEntity::class,
        AppointmentEntity::class,
        ProviderEntity::class,
        SymptomEntity::class,
        SafetyAlertEntity::class,
        ReminderEntity::class,
        AttachmentEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun pregnancyDao(): PregnancyDao
    abstract fun medicineDao(): MedicineDao
    abstract fun prescriptionDao(): PrescriptionDao
    abstract fun testDao(): TestDao
    abstract fun vitalDao(): VitalDao
    abstract fun vaccinationDao(): VaccinationDao
    abstract fun appointmentDao(): AppointmentDao
    abstract fun providerDao(): ProviderDao
    abstract fun symptomDao(): SymptomDao
    abstract fun safetyAlertDao(): SafetyAlertDao
    abstract fun reminderDao(): ReminderDao
    abstract fun attachmentDao(): AttachmentDao
}
