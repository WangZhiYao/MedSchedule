package io.floriax.medschedule.domain.model

import java.time.Instant

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/4
 */
data class TakenMedicationDetail(
    val id: Long = 0,
    val medication: Medication,
    val medicationRecordId: Long,
    val dose: Float,
    val createdAt: Instant = Instant.now(),
)