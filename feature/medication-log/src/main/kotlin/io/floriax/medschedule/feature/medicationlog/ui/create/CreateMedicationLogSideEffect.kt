package io.floriax.medschedule.feature.medicationlog.ui.create

import io.floriax.medschedule.core.domain.model.MedicationLog

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/2
 */
sealed class CreateMedicationLogSideEffect

data object NavigateToAddMedication : CreateMedicationLogSideEffect()

data object TakenMedicationsEmpty : CreateMedicationLogSideEffect()

data class CreateMedicationLogSuccess(val medicationLog: MedicationLog) :
    CreateMedicationLogSideEffect()

data object CreateMedicationLogFailure : CreateMedicationLogSideEffect()