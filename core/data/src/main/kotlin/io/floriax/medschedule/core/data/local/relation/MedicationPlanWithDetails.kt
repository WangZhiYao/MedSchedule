package io.floriax.medschedule.core.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import io.floriax.medschedule.core.data.local.entity.MedicationPlanEntity
import io.floriax.medschedule.core.data.local.entity.MedicationScheduleEntity

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/26
 */
data class MedicationPlanWithDetails(
    @Embedded
    val medicationPlan: MedicationPlanEntity,
    @Relation(
        entity = MedicationScheduleEntity::class,
        parentColumn = "id",
        entityColumn = "medication_plan_id"
    )
    val medicationSchedule: MedicationScheduleWithDetails
)