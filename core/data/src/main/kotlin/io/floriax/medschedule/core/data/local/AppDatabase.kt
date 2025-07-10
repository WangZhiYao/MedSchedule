package io.floriax.medschedule.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import io.floriax.medschedule.core.data.local.dao.MedicationDao
import io.floriax.medschedule.core.data.local.dao.MedicationRecordDao
import io.floriax.medschedule.core.data.local.dao.MedicationRecordEntryDao
import io.floriax.medschedule.core.data.local.entity.MedicationEntity
import io.floriax.medschedule.core.data.local.entity.MedicationRecordEntity
import io.floriax.medschedule.core.data.local.entity.MedicationRecordEntryEntity

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/27
 */
@Database(
    entities = [
        MedicationEntity::class,
        MedicationRecordEntity::class,
        MedicationRecordEntryEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun medicationDao(): MedicationDao

    abstract fun medicationRecordDao(): MedicationRecordDao

    abstract fun medicationRecordEntryDao(): MedicationRecordEntryDao

}