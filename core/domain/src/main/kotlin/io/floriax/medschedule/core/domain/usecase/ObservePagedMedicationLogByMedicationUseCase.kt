package io.floriax.medschedule.core.domain.usecase

import androidx.paging.PagingData
import io.floriax.medschedule.core.domain.model.MedicationLog
import io.floriax.medschedule.core.domain.repository.MedicationLogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/13
 */
class ObservePagedMedicationLogByMedicationUseCase @Inject constructor(
    private val medicationLogRepository: MedicationLogRepository
) : FlowUseCase<Long, PagingData<MedicationLog>> {

    override operator fun invoke(params: Long): Flow<PagingData<MedicationLog>> =
        medicationLogRepository.observePagedByMedicationId(params)

}