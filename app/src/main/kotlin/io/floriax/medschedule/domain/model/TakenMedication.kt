package io.floriax.medschedule.domain.model

import java.time.Instant

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/4
 */
data class TakenMedication(
    val id: Long = 0,
    val medicationId: Long,
    val medicationRecordId: Long,
    val dose: Float,
    val createdAt: Instant = Instant.now(),
)