package io.floriax.medschedule.core.domain.usecase

import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.core.domain.repository.MedicationRepository
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
class DeleteMedicationUseCase @Inject constructor(
    private val medicationRepository: MedicationRepository
) : SuspendUseCase<Medication, Boolean> {

    override suspend operator fun invoke(params: Medication): Boolean =
        medicationRepository.delete(params)

}