package io.floriax.medschedule.core.domain.usecase

import androidx.paging.PagingData
import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.core.domain.repository.MedicationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
class ObservePagedMedicationsUseCase @Inject constructor(
    private val medicationRepository: MedicationRepository
) {

    operator fun invoke(): Flow<PagingData<Medication>> =
        medicationRepository.observePaged()

}