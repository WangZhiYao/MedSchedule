package io.floriax.medschedule.domain.model

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/1
 */
data class MedicationRecordDetail(
    val record: MedicationRecord,
    val takenMedications: List<TakenMedicationDetail>
)