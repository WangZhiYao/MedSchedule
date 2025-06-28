package io.floriax.medschedule.core.domain.usecase

import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.core.domain.repository.MedicationRepository
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/31
 */
class GetMedicationByIdUseCase @Inject constructor(
    private val medicationRepository: MedicationRepository
) {

    suspend operator fun invoke(id: Long): Medication? =
        medicationRepository.getById(id)

}