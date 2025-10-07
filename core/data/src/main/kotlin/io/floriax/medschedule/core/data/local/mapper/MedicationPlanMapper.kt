package io.floriax.medschedule.core.data.local.mapper

import io.floriax.medschedule.core.data.local.entity.MedicationDoseEntity
import io.floriax.medschedule.core.data.local.entity.MedicationIntakeEntity
import io.floriax.medschedule.core.data.local.entity.MedicationPlanEntity
import io.floriax.medschedule.core.data.local.entity.MedicationScheduleEntity
import io.floriax.medschedule.core.data.local.relation.MedicationDoseWithMedication
import io.floriax.medschedule.core.data.local.relation.MedicationIntakeWithDoses
import io.floriax.medschedule.core.data.local.relation.MedicationPlanWithDetails
import io.floriax.medschedule.core.data.local.relation.MedicationScheduleWithDetails
import io.floriax.medschedule.core.domain.enums.MedicationScheduleType
import io.floriax.medschedule.core.domain.model.MedicationDose
import io.floriax.medschedule.core.domain.model.MedicationIntake
import io.floriax.medschedule.core.domain.model.MedicationPlan
import io.floriax.medschedule.core.domain.model.MedicationSchedule
import kotlinx.serialization.json.Json
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

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

fun MedicationPlanWithDetails.toModel(): MedicationPlan {
    return MedicationPlan(
        id = medicationPlan.id,
        name = medicationPlan.name,
        schedule = medicationSchedule.toModel(),
        notes = medicationPlan.notes,
        active = medicationPlan.active,
        createdAt = Instant.ofEpochMilli(medicationPlan.createdAt)
    )
}

fun MedicationScheduleWithDetails.toModel(): MedicationSchedule {
    val scheduleEntity = this.schedule
    val intakes = this.intakes.map { it.toModel() }

    return when (MedicationScheduleType.fromValue(scheduleEntity.scheduleType)) {
        MedicationScheduleType.ONE_TIME -> MedicationSchedule.OneTime(
            date = LocalDate.parse(scheduleEntity.oneTimeScheduleDate),
            intakes = intakes
        )

        MedicationScheduleType.DAILY -> MedicationSchedule.Repetitive.Daily(
            startDate = LocalDate.parse(scheduleEntity.startDate),
            endDate = scheduleEntity.endDate?.let { LocalDate.parse(it) },
            intakes = intakes
        )

        MedicationScheduleType.WEEKLY -> MedicationSchedule.Repetitive.Weekly(
            startDate = LocalDate.parse(scheduleEntity.startDate),
            endDate = scheduleEntity.endDate?.let { LocalDate.parse(it) },
            daysOfWeek = Json.decodeFromString<Set<Int>>(scheduleEntity.daysOfWeek!!)
                .map { DayOfWeek.of(it) }.toSet(),
            intakes = intakes
        )

        MedicationScheduleType.INTERVAL -> MedicationSchedule.Repetitive.Interval(
            startDate = LocalDate.parse(scheduleEntity.startDate),
            endDate = scheduleEntity.endDate?.let { LocalDate.parse(it) },
            intervalDays = scheduleEntity.intervalDays!!,
            intakes = intakes
        )

        MedicationScheduleType.CUSTOM_CYCLE -> {
            val cycleDays = this.intakes.groupBy { it.medicationIntake.cycleDay!! }
                .map { (day, intakeWithDoses) ->
                    MedicationSchedule.Repetitive.CustomCycle.CustomCycleDay(
                        dayOfCycle = day,
                        intakes = intakeWithDoses.map { it.toModel() }
                    )
                }

            MedicationSchedule.Repetitive.CustomCycle(
                startDate = LocalDate.parse(scheduleEntity.startDate),
                endDate = scheduleEntity.endDate?.let { LocalDate.parse(it) },
                cycleLengthInDays = scheduleEntity.cycleLengthInDays!!,
                cycleDays = cycleDays
            )
        }

        MedicationScheduleType.UNKNOWN -> throw IllegalStateException("Unknown schedule type")
    }
}

fun MedicationIntakeWithDoses.toModel(): MedicationIntake {
    return MedicationIntake(
        time = LocalTime.ofNanoOfDay(medicationIntake.time),
        medicationDoses = medicationDoses.map { it.toModel() }
    )
}

fun MedicationDoseWithMedication.toModel(): MedicationDose {
    return MedicationDose(
        medication = medication.toModel(),
        dose = medicationDose.dose.toBigDecimal()
    )
}