package io.floriax.medschedule.feature.medicationrecord.ui.create

import io.floriax.medschedule.core.domain.model.MedicationRecord

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/2
 */
sealed class CreateMedicationRecordSideEffect

data object NavigateToAddMedication : CreateMedicationRecordSideEffect()

data object TakenMedicationsEmpty : CreateMedicationRecordSideEffect()

data class CreateMedicationRecordSuccess(val medicationRecord: MedicationRecord) :
    CreateMedicationRecordSideEffect()

data object CreateMedicationRecordFailure : CreateMedicationRecordSideEffect()