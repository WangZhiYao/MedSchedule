package io.floriax.medschedule.feature.medicationlog.ui.detail

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/14
 */
sealed class MedicationLogDetailSideEffect

data object DeleteMedicationLogSuccess : MedicationLogDetailSideEffect()

data object DeleteMedicationLogFailure : MedicationLogDetailSideEffect()