package io.floriax.medschedule.domain.usecase

import io.floriax.medschedule.domain.model.Medication
import io.floriax.medschedule.domain.repository.MedicationRepository
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