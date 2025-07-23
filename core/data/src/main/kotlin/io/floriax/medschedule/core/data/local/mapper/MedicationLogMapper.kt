package io.floriax.medschedule.core.data.local.mapper

import io.floriax.medschedule.core.common.extension.nullIfBlank
import io.floriax.medschedule.core.data.local.entity.MedicationLogEntity
import io.floriax.medschedule.core.data.local.entity.MedicationLogEntryEntity
import io.floriax.medschedule.core.data.local.relation.MedicationLogEntryWithMedication
import io.floriax.medschedule.core.data.local.relation.MedicationLogWithEntries
import io.floriax.medschedule.core.domain.enums.MedicationLogType
import io.floriax.medschedule.core.domain.enums.MedicationState
import io.floriax.medschedule.core.domain.model.MedicationLog
import io.floriax.medschedule.core.domain.model.TakenMedication
import java.time.Instant
import java.time.ZoneId

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/1
 */
fun MedicationLogWithEntries.toModel(): MedicationLog =
    MedicationLog(
        id = log.id,
        medicationTime = Instant.ofEpochMilli(log.medicationTime),
        takenMedications = entries.map { entry -> entry.toModel() },
        state = MedicationState.fromValue(log.state),
        type = MedicationLogType.fromValue(log.type),
        timeZone = ZoneId.of(log.timeZone),
        notes = log.notes,
        createdAt = Instant.ofEpochMilli(log.createdAt)
    )

fun MedicationLog.toEntity(): MedicationLogEntity =
    MedicationLogEntity(
        id = id,
        medicationTime = medicationTime.toEpochMilli(),
        state = state.value,
        type = type.value,
        timeZone = timeZone.id,
        notes = notes?.nullIfBlank(),
        createdAt = createdAt.toEpochMilli()
    )

fun MedicationLogEntryWithMedication.toModel(): TakenMedication =
    TakenMedication(medication.toModel(), entry.dose.toBigDecimal(), entry.deductFromStock)

fun TakenMedication.toEntity(medicationLogId: Long): MedicationLogEntryEntity =
    MedicationLogEntryEntity(
        medicationLogId = medicationLogId,
        medicationId = medication.id,
        dose = dose.toPlainString(),
        deductFromStock = deductFromStock
    )
