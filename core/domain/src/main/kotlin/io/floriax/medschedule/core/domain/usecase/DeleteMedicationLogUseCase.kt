package io.floriax.medschedule.core.domain.usecase

import io.floriax.medschedule.core.domain.model.MedicationLog
import io.floriax.medschedule.core.domain.repository.MedicationLogRepository
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/14
 */
class DeleteMedicationLogUseCase @Inject constructor(
    private val medicationLogRepository: MedicationLogRepository
) : SuspendUseCase<MedicationLog, Boolean> {

    override suspend operator fun invoke(params: MedicationLog): Boolean =
        medicationLogRepository.delete(params)

}