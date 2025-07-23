package io.floriax.medschedule.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import io.floriax.medschedule.core.data.local.entity.MedicationLogEntryEntity

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/1
 */
@Dao
interface MedicationLogEntryDao : BaseDao<MedicationLogEntryEntity> {

    @Insert
    suspend fun insertBatch(items: List<MedicationLogEntryEntity>)

}