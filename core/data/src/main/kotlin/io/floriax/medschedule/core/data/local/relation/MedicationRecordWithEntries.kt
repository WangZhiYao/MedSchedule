package io.floriax.medschedule.core.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import io.floriax.medschedule.core.data.local.entity.MedicationRecordEntity
import io.floriax.medschedule.core.data.local.entity.MedicationRecordEntryEntity

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/1
 */
data class MedicationRecordWithEntries(
    @Embedded val record: MedicationRecordEntity,
    @Relation(
        entity = MedicationRecordEntryEntity::class,
        parentColumn = "id",
        entityColumn = "medication_record_id"
    )
    val entries: List<MedicationRecordEntryWithMedication>
)