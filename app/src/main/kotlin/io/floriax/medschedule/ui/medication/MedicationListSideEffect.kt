package io.floriax.medschedule.ui.medication

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
sealed class MedicationListSideEffect

data object DeleteMedicationSuccess : MedicationListSideEffect()

data object DeleteMedicationFailure : MedicationListSideEffect()
