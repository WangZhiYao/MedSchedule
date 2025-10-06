package io.floriax.medschedule.core.domain.repository

import io.floriax.medschedule.core.domain.model.MedicationPlan

/**
 *
 * @author WangZhiYao
 * @since 2025/10/5
 */
interface MedicationPlanRepository {

    suspend fun createMedicationPlan(plan: MedicationPlan)
}
