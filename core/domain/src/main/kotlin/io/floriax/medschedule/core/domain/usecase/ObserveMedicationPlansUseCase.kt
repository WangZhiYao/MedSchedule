package io.floriax.medschedule.core.domain.usecase

import io.floriax.medschedule.core.domain.model.MedicationPlan
import io.floriax.medschedule.core.domain.repository.MedicationPlanRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/10/6
 */
class ObserveMedicationPlansUseCase @Inject constructor(
    private val medicationPlanRepository: MedicationPlanRepository
) : UseCase {

    operator fun invoke(): Flow<List<MedicationPlan>> = medicationPlanRepository.observeAll()

}
