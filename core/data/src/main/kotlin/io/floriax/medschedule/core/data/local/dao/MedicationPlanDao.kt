package io.floriax.medschedule.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import io.floriax.medschedule.core.data.local.entity.MedicationPlanEntity
import io.floriax.medschedule.core.data.local.relation.MedicationPlanWithDetails
import kotlinx.coroutines.flow.Flow

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/26
 */
@Dao
interface MedicationPlanDao : BaseDao<MedicationPlanEntity> {

    @Transaction
    @Query("SELECT * FROM medication_plan")
    fun observeAll(): Flow<List<MedicationPlanWithDetails>>

}