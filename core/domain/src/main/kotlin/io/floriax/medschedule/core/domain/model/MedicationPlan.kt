package io.floriax.medschedule.core.domain.model

import java.time.Instant

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/25
 */
data class MedicationPlan(
    val id: Long = 0,
    val name: String,
    val schedule: MedicationSchedule,
    val notes: String?,
    val active: Boolean = true,
    val createdAt: Instant = Instant.now()
)