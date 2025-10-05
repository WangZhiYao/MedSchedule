package io.floriax.medschedule.feature.medicationplan.ui.create

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.floriax.medschedule.core.common.extension.mapAt
import io.floriax.medschedule.core.domain.enums.MedicationScheduleType
import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.core.domain.usecase.ObservePagedMedicationsUseCase
import io.floriax.medschedule.shared.ui.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/25
 */
@HiltViewModel
class CreateMedicationPlanViewModel @Inject constructor(
    observePagedMedicationsUseCase: ObservePagedMedicationsUseCase,
) : BaseViewModel<CreateMedicationPlanUiState, CreateMedicationPlanSideEffect>() {

    override val initialState: CreateMedicationPlanUiState
        get() = CreateMedicationPlanUiState()

    val medications: Flow<PagingData<Medication>> =
        observePagedMedicationsUseCase()
            .cachedIn(viewModelScope)

    fun onPreviousStepClick() {
        currentState.currentStep.previous()?.let(::updateStep)
    }

    fun onNextStepClick() {
        if (!validateStep(currentState.currentStep)) return
        currentState.currentStep.next()?.let(::updateStep)
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
            CreateMedicationPlanStep.BASIC_INFO -> validateName()
            CreateMedicationPlanStep.SCHEDULE_TYPE -> validateScheduleType()
            CreateMedicationPlanStep.DOSAGE -> validateDosage()
            CreateMedicationPlanStep.SAVE -> true
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

    private fun validateDosage(): Boolean {
        val intakes = currentState.intakes

        if (intakes.isEmpty()) {
            reduce { copy(intakesError = IntakesError.Empty) }
            return false
        }

        val validatedIntakes = getValidatedIntakes(intakes)

        val hasError = validatedIntakes.any { intake -> intake.hasErrors }

        reduce {
            copy(
                intakes = validatedIntakes,
                intakesError = null
            )
        }

        return !hasError
    }

    private fun getValidatedIntakes(intakes: List<IntakeInput>): List<IntakeInput> {
        val times = intakes.map { it.time }

        val duplicateKeys = intakes
            .map { it.time }
            .groupingBy { it.hour to it.minute }
            .eachCount()
            .filter { it.value > 1 }
            .keys

        val duplicateTimes = times.filter { it.hour to it.minute in duplicateKeys }.toSet()

        return intakes.map { intake -> validateIntake(intake, duplicateTimes) }
    }

    private fun validateIntake(intake: IntakeInput, duplicateTimes: Set<LocalTime>): IntakeInput {
        val validatedDoses = intake.doses.map { dose -> validateDose(dose) }
        return intake.copy(
            doses = validatedDoses,
            dosesError = if (intake.doses.isEmpty()) DosesError.Empty else null,
            timeError = if (intake.time in duplicateTimes) TimeError.Duplicate else null
        )
    }

    private fun validateDose(dose: DoseInput): DoseInput {
        val error = when {
            dose.medication == null || dose.dose.isBlank() -> DoseError.Empty
            dose.dose.toBigDecimalOrNull() == null -> DoseError.Invalid
            else -> null
        }
        return dose.copy(doseError = error)
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

    fun onAddIntakeClick() {
        reduce {
            copy(intakes = intakes + IntakeInput.default(), intakesError = null)
        }
    }

    fun onRemoveIntakeClick(intake: IntakeInput) {
        reduce {
            copy(intakes = intakes.filterNot { currentIntake -> currentIntake.id == intake.id })
        }
    }

    fun onTimeChange(intake: IntakeInput, time: LocalTime) {
        reduce {
            copy(intakes = intakes.mapAt(intakes.indexOf(intake)) { currentIntake ->
                currentIntake.copy(time = time, timeError = null)
            })
        }
    }

    fun onAddDoseClick(intake: IntakeInput) {
        reduce {
            copy(intakes = intakes.mapAt(intakes.indexOf(intake)) { currentIntake ->
                currentIntake.copy(
                    doses = currentIntake.doses + DoseInput(),
                    dosesError = null
                )
            })
        }
    }

    fun onRemoveDoseClick(intake: IntakeInput, dose: DoseInput) {
        reduce {
            copy(intakes = intakes.mapAt(intakes.indexOf(intake)) { currentIntake ->
                currentIntake.copy(
                    doses = currentIntake.doses.filterNot { currentDose ->
                        currentDose.id == dose.id
                    }
                )
            })
        }
    }

    fun onDoseMedicationSelected(intake: IntakeInput, dose: DoseInput, medication: Medication) {
        reduce {
            val intakeIndex = intakes.indexOfFirst { it.id == intake.id }
            if (intakeIndex == -1) {
                return@reduce this
            }

            val doseIndex = intakes[intakeIndex].doses.indexOfFirst { it.id == dose.id }
            if (doseIndex == -1) {
                return@reduce this
            }

            copy(intakes = intakes.mapAt(intakeIndex) { currentIntake ->
                currentIntake.copy(
                    doses = currentIntake.doses.mapAt(doseIndex) { currentDose ->
                        currentDose.copy(medication = medication, doseError = null)
                    }
                )
            })
        }
    }

    fun onDoseAmountChange(intake: IntakeInput, dose: DoseInput, amount: String) {
        reduce {
            val intakeIndex = intakes.indexOf(intake)
            if (intakeIndex == -1) {
                return@reduce this
            }

            val doseIndex = intakes[intakeIndex].doses.indexOf(dose)
            if (doseIndex == -1) {
                return@reduce this
            }

            copy(intakes = intakes.mapAt(intakeIndex) { currentIntake ->
                currentIntake.copy(
                    doses = currentIntake.doses.mapAt(doseIndex) { currentDose ->
                        currentDose.copy(dose = amount, doseError = null)
                    }
                )
            })
        }
    }
}