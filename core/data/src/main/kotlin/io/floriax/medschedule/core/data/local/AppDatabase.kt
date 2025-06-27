package io.floriax.medschedule.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import io.floriax.medschedule.core.data.local.dao.MedicationDao
import io.floriax.medschedule.core.data.local.entity.MedicationEntity

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/27
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