package io.floriax.medschedule.data.database.mapper

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
        remark = remark,
        createdAt = Instant.ofEpochMilli(createdAt)
    )