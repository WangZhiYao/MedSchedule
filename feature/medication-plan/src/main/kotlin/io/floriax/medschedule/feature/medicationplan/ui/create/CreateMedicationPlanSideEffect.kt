package io.floriax.medschedule.feature.medicationplan.ui.create

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/25
 */
sealed class CreateMedicationPlanSideEffect

data object SavePlanSuccess : CreateMedicationPlanSideEffect()

data object SavePlanFailure : CreateMedicationPlanSideEffect()

data object NotificationPermissionDenied : CreateMedicationPlanSideEffect()

