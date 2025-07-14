package io.floriax.medschedule.core.domain.usecase

import io.floriax.medschedule.core.domain.model.MedicationRecord
import io.floriax.medschedule.core.domain.repository.MedicationRecordRepository
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/2
 */
class CreateMedicationRecordUseCase @Inject constructor(
    private val medicationRecordRepository: MedicationRecordRepository
) : IUseCase {

    suspend operator fun invoke(medicationRecord: MedicationRecord): MedicationRecord =
        medicationRecordRepository.add(medicationRecord)

}