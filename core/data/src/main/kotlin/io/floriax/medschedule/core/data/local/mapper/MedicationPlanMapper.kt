package io.floriax.medschedule.core.data.local.mapper

import io.floriax.medschedule.core.data.local.entity.MedicationDoseEntity
import io.floriax.medschedule.core.data.local.entity.MedicationIntakeEntity
import io.floriax.medschedule.core.data.local.entity.MedicationPlanEntity
import io.floriax.medschedule.core.data.local.entity.MedicationScheduleEntity
import io.floriax.medschedule.core.domain.enums.MedicationScheduleType
import io.floriax.medschedule.core.domain.model.MedicationDose
import io.floriax.medschedule.core.domain.model.MedicationIntake
import io.floriax.medschedule.core.domain.model.MedicationPlan
import io.floriax.medschedule.core.domain.model.MedicationSchedule
import kotlinx.serialization.json.Json

/**
 *
 * @author WangZhiYao
 * @since 2025/10/5
 */
fun MedicationPlan.toEntity() = MedicationPlanEntity(
    id = id,
    name = name,
    notes = notes,
    active = active,
    createdAt = createdAt.toEpochMilli()
)

fun MedicationSchedule.toEntity(planId: Long): MedicationScheduleEntity {
    return when (this) {
        is MedicationSchedule.OneTime -> MedicationScheduleEntity(
            medicationPlanId = planId,
            scheduleType = MedicationScheduleType.ONE_TIME.value,
            oneTimeScheduleDate = date.toString(),
            startDate = null,
            endDate = null,
            daysOfWeek = null,
            intervalDays = null,
            cycleLengthInDays = null
        )

        is MedicationSchedule.Repetitive -> {
            MedicationScheduleEntity(
                medicationPlanId = planId,
                scheduleType = this.toScheduleTypeValue(),
                startDate = this.startDate.toString(),
                endDate = this.endDate?.toString(),
                oneTimeScheduleDate = null,
                daysOfWeek = (this as? MedicationSchedule.Repetitive.Weekly)?.daysOfWeek?.map { it.value }
                    ?.let { Json.encodeToString(it) },
                intervalDays = (this as? MedicationSchedule.Repetitive.Interval)?.intervalDays,
                cycleLengthInDays = (this as? MedicationSchedule.Repetitive.CustomCycle)?.cycleLengthInDays
            )
        }
    }
}

fun MedicationIntake.toEntity(scheduleId: Long, cycleDay: Int? = null) = MedicationIntakeEntity(
    medicationScheduleId = scheduleId,
    time = time.toNanoOfDay(),
    cycleDay = cycleDay
)

fun MedicationDose.toEntity(intakeId: Long) = MedicationDoseEntity(
    medicationIntakeId = intakeId,
    medicationId = medication.id,
    dose = dose.toPlainString()
)

private fun MedicationSchedule.Repetitive.toScheduleTypeValue(): Int {
    return when (this) {
        is MedicationSchedule.Repetitive.Daily -> MedicationScheduleType.DAILY.value
        is MedicationSchedule.Repetitive.CustomCycle -> MedicationScheduleType.CUSTOM_CYCLE.value
        is MedicationSchedule.Repetitive.Interval -> MedicationScheduleType.INTERVAL.value
        is MedicationSchedule.Repetitive.Weekly -> MedicationScheduleType.WEEKLY.value
    }
}
