package io.floriax.medschedule.core.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import io.floriax.medschedule.core.data.local.entity.MedicationIntakeEntity
import io.floriax.medschedule.core.data.local.entity.MedicationScheduleEntity

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/26
 */
data class MedicationScheduleWithDetails(
    @Embedded
    val schedule: MedicationScheduleEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "medication_schedule_id",
        entity = MedicationIntakeEntity::class
    )
    val intakes: List<MedicationIntakeWithDoses>,
)