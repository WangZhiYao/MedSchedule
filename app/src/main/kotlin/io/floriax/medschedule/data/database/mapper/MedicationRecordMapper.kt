package io.floriax.medschedule.data.database.mapper

import io.floriax.medschedule.data.database.entity.MedicationRecordEntity
import io.floriax.medschedule.domain.enums.MedicationRecordType
import io.floriax.medschedule.domain.enums.MedicationState
import io.floriax.medschedule.domain.model.MedicationRecord
import java.time.Instant
import java.time.ZoneId

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/1
 */
fun MedicationRecordEntity.toModel(): MedicationRecord =
    MedicationRecord(
        id = id,
        medicationId = medicationId,
        medicationTime = Instant.ofEpochMilli(medicationTime),
        dose = dose,
        remark = remark,
        state = MedicationState.fromValue(state),
        type = MedicationRecordType.fromValue(type),
        timeZone = ZoneId.of(timeZone),
        createAt = Instant.ofEpochMilli(createdAt)
    )

fun MedicationRecord.toEntity(): MedicationRecordEntity =
    MedicationRecordEntity(
        id = id,
        medicationId = medicationId,
        medicationTime = medicationTime.toEpochMilli(),
        dose = dose,
        remark = remark,
        state = state.value,
        type = type.value,
        timeZone = timeZone.id,
        createdAt = createAt.toEpochMilli()
    )