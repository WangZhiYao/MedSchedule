package io.floriax.medschedule.feature.medication.ui

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/27
 */
sealed class MedicineCabinetSideEffect

data object DeleteMedicationSuccess : MedicineCabinetSideEffect()

data object DeleteMedicationFailure : MedicineCabinetSideEffect()