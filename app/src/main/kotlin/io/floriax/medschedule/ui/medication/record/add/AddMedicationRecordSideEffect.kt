package io.floriax.medschedule.ui.medication.record.add

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/31
 */
sealed class AddMedicationRecordSideEffect

data object EmptyMedication : AddMedicationRecordSideEffect()

data object AddMedicationRecordSuccess : AddMedicationRecordSideEffect()

data object AddMedicationRecordFailed : AddMedicationRecordSideEffect()