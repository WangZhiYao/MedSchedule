package io.floriax.medschedule.ui.medication.edit

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/31
 */
sealed class EditMedicationSideEffect

data object EditMedicationSuccess : EditMedicationSideEffect()

data object EditMedicationFailed : EditMedicationSideEffect()