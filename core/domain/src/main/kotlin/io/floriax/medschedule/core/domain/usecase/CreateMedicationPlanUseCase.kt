package io.floriax.medschedule.core.domain.usecase

import io.floriax.medschedule.core.domain.model.MedicationPlan
import io.floriax.medschedule.core.domain.repository.MedicationPlanRepository
import javax.inject.Inject

class CreateMedicationPlanUseCase @Inject constructor(
    private val medicationPlanRepository: MedicationPlanRepository
) : UseCase {

    suspend operator fun invoke(plan: MedicationPlan) =
        medicationPlanRepository.createMedicationPlan(plan)

}
