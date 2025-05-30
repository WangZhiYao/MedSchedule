package io.floriax.medschedule.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.floriax.medschedule.data.database.dao.MedicationDao
import io.floriax.medschedule.data.database.entity.MedicationEntity

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
@Database(
    entities = [
        MedicationEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun medicationDao(): MedicationDao

}