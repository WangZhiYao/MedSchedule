package io.floriax.medschedule.data.database.mapper

import io.floriax.medschedule.common.ext.nullIfBlank
import io.floriax.medschedule.data.database.entity.MedicationRecordEntity
import io.floriax.medschedule.data.database.relation.MedicationRecordWithTakenMedications
import io.floriax.medschedule.data.database.relation.TakenMedicationWithMedication
import io.floriax.medschedule.domain.enums.MedicationRecordType
import io.floriax.medschedule.domain.enums.MedicationState
import io.floriax.medschedule.domain.model.MedicationRecord
import io.floriax.medschedule.domain.model.MedicationRecordDetail
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
        medicationTime = Instant.ofEpochMilli(medicationTime),
        remark = remark.orEmpty(),
        state = MedicationState.fromValue(state),
        type = MedicationRecordType.fromValue(type),
        timeZone = ZoneId.of(timeZone),
        createAt = Instant.ofEpochMilli(createdAt)
    )

fun MedicationRecord.toEntity(): MedicationRecordEntity =
    MedicationRecordEntity(
        id = id,
        medicationTime = medicationTime.toEpochMilli(),
        remark = remark.nullIfBlank(),
        state = state.value,
        type = type.value,
        timeZone = timeZone.id,
        createdAt = createAt.toEpochMilli()
    )

fun MedicationRecordWithTakenMedications.toDetail(): MedicationRecordDetail =
    MedicationRecordDetail(
        record = medicationRecord.toModel(),
        takenMedications = takenMedications.map(TakenMedicationWithMedication::toDetail)
    )