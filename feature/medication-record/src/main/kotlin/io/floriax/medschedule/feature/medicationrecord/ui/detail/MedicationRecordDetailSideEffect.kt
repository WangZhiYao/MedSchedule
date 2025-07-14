package io.floriax.medschedule.feature.medicationrecord.ui.detail

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/14
 */
sealed class MedicationRecordDetailSideEffect

data object DeleteMedicationRecordSuccess : MedicationRecordDetailSideEffect()

data object DeleteMedicationRecordFailure : MedicationRecordDetailSideEffect()