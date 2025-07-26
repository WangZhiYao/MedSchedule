package io.floriax.medschedule.core.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import io.floriax.medschedule.core.data.local.entity.MedicationDoseEntity
import io.floriax.medschedule.core.data.local.entity.MedicationEntity

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/26
 */
data class MedicationDoseWithMedication(
    @Embedded
    val medicationDose: MedicationDoseEntity,
    @Relation(
        parentColumn = "medication_id",
        entityColumn = "id"
    )
    val medication: MedicationEntity
)