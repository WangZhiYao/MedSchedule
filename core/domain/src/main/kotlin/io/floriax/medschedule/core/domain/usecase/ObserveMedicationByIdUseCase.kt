package io.floriax.medschedule.core.domain.usecase

import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.core.domain.repository.MedicationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/14
 */
class ObserveMedicationByIdUseCase @Inject constructor(
    private val medicationRepository: MedicationRepository
) : FlowUseCase<Long, Medication?> {

    override operator fun invoke(params: Long): Flow<Medication?> =
        medicationRepository.observeById(params)

}