package io.floriax.medschedule.domain.model

import io.floriax.medschedule.domain.enums.MedicationRecordType
import io.floriax.medschedule.domain.enums.MedicationState
import java.time.Instant
import java.time.ZoneId

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/1
 */
data class MedicationRecord(
    val id: Long = 0,
    val medicationTime: Instant,
    val remark: String,
    val state: MedicationState,
    val type: MedicationRecordType,
    val timeZone: ZoneId,
    val createAt: Instant = Instant.now(),
)