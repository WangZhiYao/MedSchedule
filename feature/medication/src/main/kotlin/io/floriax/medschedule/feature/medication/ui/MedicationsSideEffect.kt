package io.floriax.medschedule.feature.medication.ui

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/27
 */
sealed class MedicationsSideEffect

data object DeleteMedicationSuccess : MedicationsSideEffect()

data object DeleteMedicationFailure : MedicationsSideEffect()