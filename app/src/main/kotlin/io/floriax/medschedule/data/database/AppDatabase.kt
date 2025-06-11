package io.floriax.medschedule.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.floriax.medschedule.data.database.dao.MedicationDao
import io.floriax.medschedule.data.database.dao.MedicationRecordDao
import io.floriax.medschedule.data.database.dao.TakenMedicationDao
import io.floriax.medschedule.data.database.entity.MedicationEntity
import io.floriax.medschedule.data.database.entity.MedicationRecordEntity
import io.floriax.medschedule.data.database.entity.TakenMedicationEntity

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
@Database(
    entities = [
        MedicationEntity::class,
        MedicationRecordEntity::class,
        TakenMedicationEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun medicationDao(): MedicationDao

    abstract fun medicationRecordDao(): MedicationRecordDao

    abstract fun takenMedicationDao(): TakenMedicationDao

}