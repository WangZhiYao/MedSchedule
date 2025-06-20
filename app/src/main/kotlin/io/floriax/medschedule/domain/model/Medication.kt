package io.floriax.medschedule.domain.model

import java.time.Instant

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
data class Medication(
    val id: Long = 0,
    val name: String,
    val doseUnit: String,
    val remark: String,
    val createdAt: Instant = Instant.now()
)