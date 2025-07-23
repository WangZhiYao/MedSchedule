package io.floriax.medschedule.core.domain.model

import io.floriax.medschedule.core.domain.enums.MedicationLogType
import io.floriax.medschedule.core.domain.enums.MedicationState
import java.time.Instant
import java.time.ZoneId

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/1
 */
data class MedicationLog(
    val id: Long = 0,
    val medicationTime: Instant,
    val takenMedications: List<TakenMedication>,
    val state: MedicationState,
    val type: MedicationLogType,
    val timeZone: ZoneId,
    val notes: String?,
    val createdAt: Instant = Instant.now()
)