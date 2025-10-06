package io.floriax.medschedule.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import io.floriax.medschedule.core.data.local.entity.MedicationDoseEntity

/**
 *
 * @author WangZhiYao
 * @since 2025/10/5
 */
@Dao
interface MedicationDoseDao {

    @Insert
    suspend fun insertBatch(doses: List<MedicationDoseEntity>)

}
