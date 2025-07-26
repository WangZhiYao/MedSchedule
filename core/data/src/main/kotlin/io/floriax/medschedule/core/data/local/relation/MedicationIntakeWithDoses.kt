package io.floriax.medschedule.core.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import io.floriax.medschedule.core.data.local.entity.MedicationDoseEntity
import io.floriax.medschedule.core.data.local.entity.MedicationIntakeEntity

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/26
 */
data class MedicationIntakeWithDoses(
    @Embedded
    val medicationIntake: MedicationIntakeEntity,
    @Relation(
        entity = MedicationDoseEntity::class,
        parentColumn = "id",
        entityColumn = "medication_intake_id"
    )
    val medicationDoses: List<MedicationDoseWithMedication>
)
