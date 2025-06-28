package io.floriax.medschedule.core.domain.model

import java.math.BigDecimal
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
    val stock: BigDecimal?,
    val doseUnit: String,
    val notes: String,
    val createdAt: Instant = Instant.now()
)