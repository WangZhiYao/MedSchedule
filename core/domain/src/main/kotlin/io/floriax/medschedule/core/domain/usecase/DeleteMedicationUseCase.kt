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
) : IUseCase {

    suspend operator fun invoke(medication: Medication): Boolean =
        medicationRepository.delete(medication)

}