package io.floriax.medschedule.ui.medication.add

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/31
 */
sealed class AddMedicationSideEffect

data object AddMedicationSuccess : AddMedicationSideEffect()

data object AddMedicationFailed : AddMedicationSideEffect()