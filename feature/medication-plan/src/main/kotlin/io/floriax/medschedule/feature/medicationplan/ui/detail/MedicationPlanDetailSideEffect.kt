package io.floriax.medschedule.feature.medicationplan.ui.detail

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/10/8
 */
sealed class MedicationPlanDetailSideEffect

data object DeletePlanSuccess : MedicationPlanDetailSideEffect()

data object DeletePlanFailure : MedicationPlanDetailSideEffect()
