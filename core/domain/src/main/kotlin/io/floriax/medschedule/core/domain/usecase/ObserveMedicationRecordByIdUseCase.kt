package io.floriax.medschedule.core.domain.usecase

import io.floriax.medschedule.core.domain.model.MedicationRecord
import io.floriax.medschedule.core.domain.repository.MedicationRecordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/14
 */
class ObserveMedicationRecordByIdUseCase @Inject constructor(
    private val medicationRecordRepository: MedicationRecordRepository
) : IUseCase {

    operator fun invoke(id: Long): Flow<MedicationRecord?> =
        medicationRecordRepository.observeById(id)

}