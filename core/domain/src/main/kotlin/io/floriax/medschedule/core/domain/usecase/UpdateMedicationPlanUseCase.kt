package io.floriax.medschedule.core.domain.usecase

import io.floriax.medschedule.core.domain.model.MedicationPlan
import io.floriax.medschedule.core.domain.repository.MedicationPlanRepository
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/10/8
 */
class UpdateMedicationPlanUseCase @Inject constructor(
    private val medicationPlanRepository: MedicationPlanRepository
) : UseCase {

    suspend operator fun invoke(plan: MedicationPlan): Boolean =
        medicationPlanRepository.update(plan)
}
