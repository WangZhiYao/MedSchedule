package io.floriax.medschedule.core.domain.usecase

import androidx.paging.PagingData
import io.floriax.medschedule.core.domain.model.MedicationRecord
import io.floriax.medschedule.core.domain.repository.MedicationRecordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/13
 */
class ObservePagedMedicationRecordByMedicationUseCase @Inject constructor(
    private val medicationRecordRepository: MedicationRecordRepository
) : FlowUseCase<Long, PagingData<MedicationRecord>> {

    override operator fun invoke(params: Long): Flow<PagingData<MedicationRecord>> =
        medicationRecordRepository.observePagedByMedicationId(params)

}