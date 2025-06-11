package io.floriax.medschedule.domain.usecase

import io.floriax.medschedule.domain.model.TakenMedication
import io.floriax.medschedule.domain.repository.TakenMedicationRepository
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/9
 */
class AddTakenMedicationBatchUseCase @Inject constructor(
    private val takenMedicationRepository: TakenMedicationRepository
) {

    suspend operator fun invoke(takenMedications: List<TakenMedication>): List<TakenMedication> =
        takenMedicationRepository.addBatch(takenMedications)

}