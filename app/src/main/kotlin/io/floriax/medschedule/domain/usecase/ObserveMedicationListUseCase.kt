package io.floriax.medschedule.domain.usecase

import io.floriax.medschedule.domain.repository.MedicationRepository
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
class ObserveMedicationListUseCase @Inject constructor(
    private val medicationRepository: MedicationRepository
) {

    operator fun invoke() = medicationRepository.observeAll()

}