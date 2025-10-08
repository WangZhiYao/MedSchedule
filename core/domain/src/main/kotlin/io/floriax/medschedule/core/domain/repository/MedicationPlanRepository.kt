package io.floriax.medschedule.core.domain.repository

import io.floriax.medschedule.core.domain.model.MedicationPlan
import kotlinx.coroutines.flow.Flow

/**
 *
 * @author WangZhiYao
 * @since 2025/10/5
 */
interface MedicationPlanRepository {

    fun observeAll(): Flow<List<MedicationPlan>>

    suspend fun getById(id: Long): MedicationPlan?

    suspend fun create(plan: MedicationPlan): MedicationPlan

}
