package io.floriax.medschedule.data.database.mapper

import io.floriax.medschedule.common.ext.nullIfBlank
import io.floriax.medschedule.data.database.entity.MedicationEntity
import io.floriax.medschedule.domain.model.Medication
import java.time.Instant

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
fun MedicationEntity.toModel(): Medication =
    Medication(
        id = id,
        name = name,
        doseUnit = doseUnit,
        remark = remark.orEmpty(),
        createdAt = Instant.ofEpochMilli(createdAt)
    )

fun Medication.toEntity(): MedicationEntity =
    MedicationEntity(
        id = id,
        name = name,
        doseUnit = doseUnit,
        remark = remark.nullIfBlank(),
        createdAt = createdAt.toEpochMilli()
    )