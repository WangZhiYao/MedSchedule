package io.floriax.medschedule.data.database.mapper

import io.floriax.medschedule.data.database.entity.TakenMedicationEntity
import io.floriax.medschedule.data.database.relation.TakenMedicationWithMedication
import io.floriax.medschedule.domain.model.TakenMedication
import io.floriax.medschedule.domain.model.TakenMedicationDetail
import java.time.Instant

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/4
 */
fun TakenMedicationEntity.toModel(): TakenMedication =
    TakenMedication(
        id = id,
        medicationId = medicationId,
        medicationRecordId = medicationRecordId,
        dose = dose,
        createdAt = Instant.ofEpochMilli(createdAt)
    )

fun TakenMedication.toEntity(): TakenMedicationEntity =
    TakenMedicationEntity(
        id = id,
        medicationId = medicationId,
        medicationRecordId = medicationRecordId,
        dose = dose,
        createdAt = createdAt.toEpochMilli()
    )

fun TakenMedicationWithMedication.toDetail(): TakenMedicationDetail =
    takenMedication.run {
        TakenMedicationDetail(
            id = takenMedication.id,
            medication = medication.toModel(),
            medicationRecordId = takenMedication.medicationRecordId,
            dose = takenMedication.dose,
            createdAt = Instant.ofEpochMilli(takenMedication.createdAt)
        )
    }