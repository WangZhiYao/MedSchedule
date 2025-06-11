package io.floriax.medschedule.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import io.floriax.medschedule.data.database.entity.MedicationEntity
import io.floriax.medschedule.data.database.entity.TakenMedicationEntity

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/4
 */
data class TakenMedicationWithMedication(
    @Embedded
    val takenMedication: TakenMedicationEntity,
    @Relation(
        parentColumn = "medication_id",
        entityColumn = "id"
    )
    val medication: MedicationEntity
)