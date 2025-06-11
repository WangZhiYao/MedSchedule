package io.floriax.medschedule.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import io.floriax.medschedule.data.database.entity.MedicationRecordEntity
import io.floriax.medschedule.data.database.entity.TakenMedicationEntity

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/1
 */
data class MedicationRecordWithTakenMedications(
    @Embedded
    val medicationRecord: MedicationRecordEntity,
    @Relation(
        entity = TakenMedicationEntity::class,
        parentColumn = "id",
        entityColumn = "medication_record_id"
    )
    val takenMedications: List<TakenMedicationWithMedication>
)