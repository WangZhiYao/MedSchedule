package io.floriax.medschedule.core.domain.usecase

import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.core.domain.repository.MedicationRepository
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/31
 */
class AddMedicationUseCase @Inject constructor(
    private val medicationRepository: MedicationRepository
) : SuspendUseCase<Medication, Medication> {

    override suspend operator fun invoke(params: Medication): Medication =
        medicationRepository.add(params)

}