package io.floriax.medschedule.feature.medicationplan.ui.create

import io.floriax.medschedule.core.domain.enums.MedicationScheduleType
import java.time.DayOfWeek
import java.time.LocalDate

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/25
 */
data class CreateMedicationPlanUiState(
    val currentStep: CreateMedicationPlanStep = CreateMedicationPlanStep.BASIC_INFO,
    val showPreviousStep: Boolean = false,
    val showSaveButton: Boolean = false,
    val name: String = "",
    val nameError: NameError? = null,
    val notes: String = "",
    val scheduleTypes: List<MedicationScheduleType> = MedicationScheduleType.entries.filterNot { type -> type == MedicationScheduleType.UNKNOWN },
    val selectedScheduleType: MedicationScheduleType = MedicationScheduleType.ONE_TIME,
    val oneTimeScheduleDate: LocalDate = LocalDate.now(),
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate? = null,
    val weeklySelectedDays: Set<DayOfWeek> = emptySet(),
    val weeklyDaysError: WeekDaysError? = null,
    val intervalDays: String = "",
    val intervalDaysError: IntervalDaysError? = null,
    val customCycleDaysOn: String = "",
    val customCycleDaysOff: String = "",
    val customCycleDaysError: CustomCycleDaysError? = null
)

enum class CreateMedicationPlanStep {
    BASIC_INFO,
    SCHEDULE_TYPE,
    DOSAGE,
    SAVE
}

fun CreateMedicationPlanStep.previous(): CreateMedicationPlanStep? {
    val ordinal = this.ordinal - 1
    return if (ordinal < 0) {
        null
    } else {
        CreateMedicationPlanStep.entries[ordinal]
    }
}

fun CreateMedicationPlanStep.next(): CreateMedicationPlanStep? {
    val ordinal = this.ordinal + 1
    return if (ordinal >= CreateMedicationPlanStep.entries.size) {
        null
    } else {
        CreateMedicationPlanStep.entries[ordinal]
    }
}

fun CreateMedicationPlanStep.canShowPrevious(): Boolean = this.ordinal > 0

fun CreateMedicationPlanStep.canShowSave(): Boolean =
    this == CreateMedicationPlanStep.entries.last()

sealed class NameError {
    data object Empty : NameError()
}

sealed class WeekDaysError {
    data object Empty : WeekDaysError()
}

sealed class IntervalDaysError {
    data object Empty : IntervalDaysError()
    data object Invalid : IntervalDaysError()
}

sealed class CustomCycleDaysError {
    data object DaysOnEmpty : CustomCycleDaysError()
    data object DaysOffEmpty : CustomCycleDaysError()
    data object Invalid : CustomCycleDaysError()
}