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
 * @since 2025/7/2
 */
class ObservePagedMedicationLogsUseCase @Inject constructor(
    private val medicationLogRepository: MedicationLogRepository
) : UseCase {

    fun invoke(): Flow<PagingData<MedicationLog>> =
        medicationLogRepository.observePaged()

}