package io.floriax.medschedule.core.data.local.mapper

import io.floriax.medschedule.core.common.extension.nullIfBlank
import io.floriax.medschedule.core.data.local.entity.MedicationEntity
import io.floriax.medschedule.core.domain.model.Medication
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
        stock = stock?.toBigDecimalOrNull(),
        doseUnit = doseUnit,
        notes = notes.orEmpty(),
        createdAt = Instant.ofEpochMilli(createdAt)
    )

fun Medication.toEntity(): MedicationEntity =
    MedicationEntity(
        id = id,
        name = name,
        stock = stock?.toPlainString(),
        doseUnit = doseUnit,
        notes = notes.nullIfBlank(),
        createdAt = createdAt.toEpochMilli()
    )