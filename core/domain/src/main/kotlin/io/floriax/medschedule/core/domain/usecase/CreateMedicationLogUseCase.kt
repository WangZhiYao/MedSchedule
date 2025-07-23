package io.floriax.medschedule.core.domain.usecase

import io.floriax.medschedule.core.domain.model.MedicationLog
import io.floriax.medschedule.core.domain.repository.MedicationLogRepository
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/2
 */
class CreateMedicationLogUseCase @Inject constructor(
    private val medicationLogRepository: MedicationLogRepository
) : SuspendUseCase<MedicationLog, MedicationLog> {

    override suspend operator fun invoke(params: MedicationLog): MedicationLog =
        medicationLogRepository.add(params)

}