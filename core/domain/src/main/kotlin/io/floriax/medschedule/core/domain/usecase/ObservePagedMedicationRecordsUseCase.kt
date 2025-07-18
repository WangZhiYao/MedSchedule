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
 * @since 2025/7/2
 */
class ObservePagedMedicationRecordsUseCase @Inject constructor(
    private val medicationRecordRepository: MedicationRecordRepository
) : FlowUseCase<Unit, PagingData<MedicationRecord>> {

    override fun invoke(params: Unit): Flow<PagingData<MedicationRecord>> =
        medicationRecordRepository.observePaged()

}