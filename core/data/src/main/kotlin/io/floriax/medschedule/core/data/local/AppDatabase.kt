package io.floriax.medschedule.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import io.floriax.medschedule.core.data.local.dao.MedicationDao
import io.floriax.medschedule.core.data.local.dao.MedicationLogDao
import io.floriax.medschedule.core.data.local.dao.MedicationLogEntryDao
import io.floriax.medschedule.core.data.local.entity.MedicationEntity
import io.floriax.medschedule.core.data.local.entity.MedicationLogEntity
import io.floriax.medschedule.core.data.local.entity.MedicationLogEntryEntity

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/27
 */
@Database(
    entities = [
        MedicationEntity::class,
        MedicationLogEntity::class,
        MedicationLogEntryEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun medicationDao(): MedicationDao

    abstract fun medicationLogDao(): MedicationLogDao

    abstract fun medicationLogEntryDao(): MedicationLogEntryDao

}