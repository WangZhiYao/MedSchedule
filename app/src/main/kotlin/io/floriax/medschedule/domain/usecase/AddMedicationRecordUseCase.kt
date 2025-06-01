package io.floriax.medschedule.domain.usecase

import io.floriax.medschedule.domain.model.MedicationRecord
import io.floriax.medschedule.domain.repository.MedicationRecordRepository
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/1
 */
class AddMedicationRecordUseCase @Inject constructor(
    private val medicationRecordRepository: MedicationRecordRepository,
) {

    suspend operator fun invoke(medicationRecord: MedicationRecord): MedicationRecord =
        medicationRecordRepository.add(medicationRecord)

}