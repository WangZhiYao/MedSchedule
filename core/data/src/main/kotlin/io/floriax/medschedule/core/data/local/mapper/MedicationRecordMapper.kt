package io.floriax.medschedule.core.data.local.mapper

import io.floriax.medschedule.core.common.extension.nullIfBlank
import io.floriax.medschedule.core.data.local.entity.MedicationRecordEntity
import io.floriax.medschedule.core.data.local.entity.MedicationRecordEntryEntity
import io.floriax.medschedule.core.data.local.relation.MedicationRecordEntryWithMedication
import io.floriax.medschedule.core.data.local.relation.MedicationRecordWithEntries
import io.floriax.medschedule.core.domain.enums.MedicationRecordType
import io.floriax.medschedule.core.domain.enums.MedicationState
import io.floriax.medschedule.core.domain.model.MedicationRecord
import io.floriax.medschedule.core.domain.model.TakenMedication
import java.time.Instant
import java.time.ZoneId

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/1
 */
fun MedicationRecordWithEntries.toModel(): MedicationRecord =
    MedicationRecord(
        id = record.id,
        medicationTime = Instant.ofEpochMilli(record.medicationTime),
        takenMedications = entries.map { entry -> entry.toModel() },
        state = MedicationState.fromValue(record.state),
        type = MedicationRecordType.fromValue(record.type),
        timeZone = ZoneId.of(record.timeZone),
        notes = record.notes,
        createdAt = Instant.ofEpochMilli(record.createdAt)
    )

fun MedicationRecord.toEntity(): MedicationRecordEntity =
    MedicationRecordEntity(
        id = id,
        medicationTime = medicationTime.toEpochMilli(),
        state = state.value,
        type = type.value,
        timeZone = timeZone.id,
        notes = notes?.nullIfBlank(),
        createdAt = createdAt.toEpochMilli()
    )

fun MedicationRecordEntryWithMedication.toModel(): TakenMedication =
    TakenMedication(medication.toModel(), entry.dose.toBigDecimal())

fun TakenMedication.toEntity(medicationRecordId: Long): MedicationRecordEntryEntity =
    MedicationRecordEntryEntity(
        medicationRecordId = medicationRecordId,
        medicationId = medication.id,
        dose = dose.toPlainString()
    )
