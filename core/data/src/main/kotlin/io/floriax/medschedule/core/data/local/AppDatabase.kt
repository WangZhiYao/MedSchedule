package io.floriax.medschedule.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import io.floriax.medschedule.core.data.local.dao.MedicationDao
import io.floriax.medschedule.core.data.local.dao.MedicationDoseDao
import io.floriax.medschedule.core.data.local.dao.MedicationIntakeDao
import io.floriax.medschedule.core.data.local.dao.MedicationLogDao
import io.floriax.medschedule.core.data.local.dao.MedicationLogEntryDao
import io.floriax.medschedule.core.data.local.dao.MedicationPlanDao
import io.floriax.medschedule.core.data.local.dao.MedicationScheduleDao
import io.floriax.medschedule.core.data.local.entity.MedicationDoseEntity
import io.floriax.medschedule.core.data.local.entity.MedicationEntity
import io.floriax.medschedule.core.data.local.entity.MedicationIntakeEntity
import io.floriax.medschedule.core.data.local.entity.MedicationLogEntity
import io.floriax.medschedule.core.data.local.entity.MedicationLogEntryEntity
import io.floriax.medschedule.core.data.local.entity.MedicationPlanEntity
import io.floriax.medschedule.core.data.local.entity.MedicationScheduleEntity

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/27
 */
@Database(
    entities = [
        MedicationEntity::class,
        MedicationPlanEntity::class,
        MedicationScheduleEntity::class,
        MedicationIntakeEntity::class,
        MedicationDoseEntity::class,
        MedicationLogEntity::class,
        MedicationLogEntryEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun medicationDao(): MedicationDao

    abstract fun medicationPlanDao(): MedicationPlanDao

    abstract fun medicationScheduleDao(): MedicationScheduleDao

    abstract fun medicationIntakeDao(): MedicationIntakeDao

    abstract fun medicationDoseDao(): MedicationDoseDao

    abstract fun medicationLogDao(): MedicationLogDao

    abstract fun medicationLogEntryDao(): MedicationLogEntryDao

}