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
) : FlowUseCase<Long, MedicationRecord?> {

    override operator fun invoke(params: Long): Flow<MedicationRecord?> =
        medicationRecordRepository.observeById(params)

}