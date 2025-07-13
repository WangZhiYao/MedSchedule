package io.floriax.medschedule.feature.medication.ui.detail

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/10
 */
sealed class MedicationDetailSideEffect

data object DeleteMedicationSuccess : MedicationDetailSideEffect()

data object DeleteMedicationFailure : MedicationDetailSideEffect()

data object AddStockSuccess : MedicationDetailSideEffect()

data object AddStockFailure : MedicationDetailSideEffect()