package io.floriax.medschedule.core.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import io.floriax.medschedule.core.data.local.entity.MedicationEntity
import io.floriax.medschedule.core.data.local.entity.MedicationLogEntryEntity

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/1
 */
data class MedicationLogEntryWithMedication(
    @Embedded val entry: MedicationLogEntryEntity,
    @Relation(
        parentColumn = "medication_id",
        entityColumn = "id"
    )
    val medication: MedicationEntity
)