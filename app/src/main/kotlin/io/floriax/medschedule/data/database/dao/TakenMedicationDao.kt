package io.floriax.medschedule.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import io.floriax.medschedule.data.database.entity.TakenMedicationEntity

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/4
 */
@Dao
interface TakenMedicationDao : IDao<TakenMedicationEntity> {

    @Insert
    suspend fun insertBatch(takenMedications: List<TakenMedicationEntity>): List<Long>

}