package io.floriax.medschedule.core.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import io.floriax.medschedule.core.data.local.entity.MedicationLogEntity
import io.floriax.medschedule.core.data.local.entity.MedicationLogEntryEntity

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/1
 */
data class MedicationLogWithEntries(
    @Embedded val log: MedicationLogEntity,
    @Relation(
        entity = MedicationLogEntryEntity::class,
        parentColumn = "id",
        entityColumn = "medication_log_id"
    )
    val entries: List<MedicationLogEntryWithMedication>
)