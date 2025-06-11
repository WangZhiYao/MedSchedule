package io.floriax.medschedule.domain.usecase

import androidx.paging.PagingData
import io.floriax.medschedule.domain.model.MedicationRecordDetail
import io.floriax.medschedule.domain.repository.MedicationRecordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/1
 */
class ObservePagedMedicationRecordDetailUseCase @Inject constructor(
    private val medicationRecordRepository: MedicationRecordRepository
) {

    operator fun invoke(): Flow<PagingData<MedicationRecordDetail>> =
        medicationRecordRepository.observePagedMedicationRecordDetail()

}