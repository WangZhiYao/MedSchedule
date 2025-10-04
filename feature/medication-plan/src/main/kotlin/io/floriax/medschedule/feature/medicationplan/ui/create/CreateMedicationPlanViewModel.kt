package io.floriax.medschedule.feature.medicationplan.ui.create

import dagger.hilt.android.lifecycle.HiltViewModel
import io.floriax.medschedule.core.domain.enums.MedicationScheduleType
import io.floriax.medschedule.shared.ui.base.BaseViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/25
 */
@HiltViewModel
class CreateMedicationPlanViewModel @Inject constructor(

) : BaseViewModel<CreateMedicationPlanUiState, CreateMedicationPlanSideEffect>() {

    override val initialState: CreateMedicationPlanUiState
        get() = CreateMedicationPlanUiState()

    fun onPreviousStepClick() {
        currentState.currentStep.previous()?.let(::updateStep)
    }

    fun onNextStepClick() {
        val nextStep = currentState.currentStep.next() ?: return
        if (!validateStep(nextStep)) return
        updateStep(nextStep)
    }

    private fun updateStep(step: CreateMedicationPlanStep) {
        reduce {
            copy(
                currentStep = step,
                showPreviousStep = step.canShowPrevious(),
                showSaveButton = step.canShowSave()
            )
        }
    }

    private fun validateStep(step: CreateMedicationPlanStep): Boolean {
        return when (step) {
            CreateMedicationPlanStep.SCHEDULE_TYPE -> validateName()
            CreateMedicationPlanStep.DOSAGE -> validateScheduleType()
            else -> true
        }
    }

    private fun validateName(): Boolean {
        if (currentState.name.isBlank()) {
            reduce { copy(nameError = NameError.Empty) }
            return false
        }
        return true
    }

    private fun validateScheduleType(): Boolean {
        return when (currentState.selectedScheduleType) {
            MedicationScheduleType.WEEKLY -> validateWeekly()
            MedicationScheduleType.INTERVAL -> validateInterval()
            MedicationScheduleType.CUSTOM_CYCLE -> validateCustomCycle()
            else -> true
        }
    }

    private fun validateWeekly(): Boolean {
        if (currentState.weeklySelectedDays.isEmpty()) {
            reduce { copy(weeklyDaysError = WeekDaysError.Empty) }
            return false
        }
        return true
    }

    private fun validateInterval(): Boolean {
        val days = currentState.intervalDays
        val error = when {
            days.isEmpty() -> IntervalDaysError.Empty
            days.toIntOrNull() == null -> IntervalDaysError.Invalid
            else -> null
        }
        reduce { copy(intervalDaysError = error) }
        return error == null
    }

    private fun validateCustomCycle(): Boolean {
        val on = currentState.customCycleDaysOn
        val off = currentState.customCycleDaysOff
        val error = when {
            on.isEmpty() -> CustomCycleDaysError.DaysOnEmpty
            off.isEmpty() -> CustomCycleDaysError.DaysOffEmpty
            on.toIntOrNull() == null || off.toIntOrNull() == null -> CustomCycleDaysError.Invalid
            else -> null
        }
        reduce { copy(customCycleDaysError = error) }
        return error == null
    }

    fun onNameChange(name: String) {
        reduce {
            copy(name = name, nameError = if (name.isBlank()) NameError.Empty else null)
        }
    }

    fun onNotesChange(notes: String) {
        reduce {
            copy(notes = notes)
        }
    }

    fun onScheduleTypeChange(selectedScheduleType: MedicationScheduleType) {
        reduce {
            copy(
                selectedScheduleType = selectedScheduleType,
                weeklyDaysError = null,
                intervalDaysError = null,
                customCycleDaysError = null
            )
        }
    }

    fun onOneTimeScheduleDateChange(date: LocalDate) {
        reduce {
            copy(oneTimeScheduleDate = date)
        }
    }

    fun onStartDateChange(date: LocalDate) {
        reduce {
            copy(startDate = date)
        }
    }

    fun onEndDateChange(date: LocalDate?) {
        reduce {
            copy(endDate = date)
        }
    }

    fun onWeeklyDaySelected(day: DayOfWeek) {
        reduce {
            val selectedDays = weeklySelectedDays.toMutableSet()
            if (selectedDays.contains(day)) {
                selectedDays.remove(day)
            } else {
                selectedDays.add(day)
            }
            copy(weeklySelectedDays = selectedDays, weeklyDaysError = null)
        }
    }

    fun onIntervalDaysChange(days: String) {
        reduce {
            copy(intervalDays = days, intervalDaysError = null)
        }
    }

    fun onCustomCycleDaysOnChange(days: String) {
        reduce {
            copy(customCycleDaysOn = days, customCycleDaysError = null)
        }
    }

    fun onCustomCycleDaysOffChange(days: String) {
        reduce {
            copy(customCycleDaysOff = days, customCycleDaysError = null)
        }
    }
}