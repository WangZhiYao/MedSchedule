package io.floriax.medschedule.core.domain.usecase

import io.floriax.medschedule.core.domain.model.MedicationLog
import io.floriax.medschedule.core.domain.repository.MedicationLogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/14
 */
class ObserveMedicationLogByIdUseCase @Inject constructor(
    private val medicationLogRepository: MedicationLogRepository
) : UseCase {

    operator fun invoke(id: Long): Flow<MedicationLog?> =
        medicationLogRepository.observeById(id)

}