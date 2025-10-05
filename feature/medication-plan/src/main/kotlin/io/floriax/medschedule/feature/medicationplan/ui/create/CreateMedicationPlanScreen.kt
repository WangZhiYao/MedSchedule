package io.floriax.medschedule.feature.medicationplan.ui.create

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.floriax.medschedule.core.domain.enums.MedicationScheduleType
import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.feature.medicationplan.R
import io.floriax.medschedule.feature.medicationplan.ui.create.steps.BasicInfoStep
import io.floriax.medschedule.feature.medicationplan.ui.create.steps.DosageStep
import io.floriax.medschedule.feature.medicationplan.ui.create.steps.ScheduleTypeStep
import io.floriax.medschedule.shared.designsystem.icon.AppIcons
import io.floriax.medschedule.shared.designsystem.theme.AppTheme
import io.floriax.medschedule.shared.ui.component.BackButton
import io.floriax.medschedule.shared.ui.extension.collectState
import kotlinx.coroutines.flow.flowOf
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import io.floriax.medschedule.shared.ui.R as sharedUiR

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/10/3
 */
@Composable
fun CreateMedicationPlanRoute(
    onBackClick: () -> Unit,
    viewModel: CreateMedicationPlanViewModel = hiltViewModel()
) {
    val uiState by viewModel.collectState()

    val medications = viewModel.medications.collectAsLazyPagingItems()

    CreateMedicationPlanScreen(
        uiState = uiState,
        medications = medications,
        onBackClick = onBackClick,
        onPreviousClick = viewModel::onPreviousStepClick,
        onNextClick = viewModel::onNextStepClick,
        onNameChange = viewModel::onNameChange,
        onNotesChange = viewModel::onNotesChange,
        onScheduleTypeChange = viewModel::onScheduleTypeChange,
        onOneTimeScheduleDateChange = viewModel::onOneTimeScheduleDateChange,
        onStartDateChange = viewModel::onStartDateChange,
        onEndDateChange = viewModel::onEndDateChange,
        onWeeklyDaySelected = viewModel::onWeeklyDaySelected,
        onIntervalDaysChange = viewModel::onIntervalDaysChange,
        onCustomCycleDaysOnChange = viewModel::onCustomCycleDaysOnChange,
        onCustomCycleDaysOffChange = viewModel::onCustomCycleDaysOffChange,
        onAddIntakeClick = viewModel::onAddIntakeClick,
        onRemoveIntakeClick = viewModel::onRemoveIntakeClick,
        onTimeChange = viewModel::onTimeChange,
        onAddDoseClick = viewModel::onAddDoseClick,
        onRemoveDoseClick = viewModel::onRemoveDoseClick,
        onDoseAmountChange = viewModel::onDoseAmountChange,
        onDoseMedicationSelected = viewModel::onDoseMedicationSelected
    )
}

@Composable
private fun CreateMedicationPlanScreen(
    uiState: CreateMedicationPlanUiState,
    medications: LazyPagingItems<Medication>,
    onBackClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onNameChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onScheduleTypeChange: (MedicationScheduleType) -> Unit,
    onOneTimeScheduleDateChange: (LocalDate) -> Unit,
    onStartDateChange: (LocalDate) -> Unit,
    onEndDateChange: (LocalDate?) -> Unit,
    onWeeklyDaySelected: (DayOfWeek) -> Unit,
    onIntervalDaysChange: (String) -> Unit,
    onCustomCycleDaysOnChange: (String) -> Unit,
    onCustomCycleDaysOffChange: (String) -> Unit,
    onAddIntakeClick: () -> Unit,
    onRemoveIntakeClick: (IntakeInput) -> Unit,
    onTimeChange: (IntakeInput, LocalTime) -> Unit,
    onAddDoseClick: (IntakeInput) -> Unit,
    onRemoveDoseClick: (IntakeInput, DoseInput) -> Unit,
    onDoseAmountChange: (IntakeInput, DoseInput, String) -> Unit,
    onDoseMedicationSelected: (IntakeInput, DoseInput, Medication) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CreateMedicationPlanTopBar(onBackClick = onBackClick)
        },
        bottomBar = {
            CreateMedicationPlanBottomBar(
                uiState = uiState,
                onPreviousClick = onPreviousClick,
                onNextClick = onNextClick
            )
        }
    ) { paddingValues ->
        CreateMedicationPlanContent(
            uiState = uiState,
            medications = medications,
            onNameChange = onNameChange,
            onNotesChange = onNotesChange,
            onScheduleTypeChange = onScheduleTypeChange,
            onOneTimeScheduleDateChange = onOneTimeScheduleDateChange,
            onStartDateChange = onStartDateChange,
            onEndDateChange = onEndDateChange,
            onWeeklyDaySelected = onWeeklyDaySelected,
            onIntervalDaysChange = onIntervalDaysChange,
            onCustomCycleDaysOnChange = onCustomCycleDaysOnChange,
            onCustomCycleDaysOffChange = onCustomCycleDaysOffChange,
            onAddIntakeClick = onAddIntakeClick,
            onRemoveIntakeClick = onRemoveIntakeClick,
            onTimeChange = onTimeChange,
            onAddDoseClick = onAddDoseClick,
            onRemoveDoseClick = onRemoveDoseClick,
            onDoseAmountChange = onDoseAmountChange,
            onDoseMedicationSelected = onDoseMedicationSelected,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateMedicationPlanTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.screen_create_medication_plan_title))
        },
        modifier = modifier,
        navigationIcon = {
            BackButton(onClick = onBackClick)
        }
    )
}

@Composable
private fun CreateMedicationPlanContent(
    uiState: CreateMedicationPlanUiState,
    medications: LazyPagingItems<Medication>,
    onNameChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onScheduleTypeChange: (MedicationScheduleType) -> Unit,
    onOneTimeScheduleDateChange: (LocalDate) -> Unit,
    onStartDateChange: (LocalDate) -> Unit,
    onEndDateChange: (LocalDate?) -> Unit,
    onWeeklyDaySelected: (DayOfWeek) -> Unit,
    onIntervalDaysChange: (String) -> Unit,
    onCustomCycleDaysOnChange: (String) -> Unit,
    onCustomCycleDaysOffChange: (String) -> Unit,
    onAddIntakeClick: () -> Unit,
    onRemoveIntakeClick: (IntakeInput) -> Unit,
    onTimeChange: (IntakeInput, LocalTime) -> Unit,
    onAddDoseClick: (IntakeInput) -> Unit,
    onRemoveDoseClick: (IntakeInput, DoseInput) -> Unit,
    onDoseAmountChange: (IntakeInput, DoseInput, String) -> Unit,
    onDoseMedicationSelected: (IntakeInput, DoseInput, Medication) -> Unit,
    modifier: Modifier = Modifier
) {
    val progress =
        (uiState.currentStep.ordinal + 1) / CreateMedicationPlanStep.entries.size.toFloat()
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
    )

    Column(modifier = modifier.fillMaxSize()) {
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxWidth()
        )
        AnimatedContent(
            targetState = uiState.currentStep,
            label = "CreateMedicationPlanStepAnimation",
            transitionSpec = {
                if (targetState.ordinal > initialState.ordinal) {
                    slideInHorizontally { fullWidth -> fullWidth } + fadeIn() togetherWith
                            slideOutHorizontally { fullWidth -> -fullWidth } + fadeOut()
                } else {
                    slideInHorizontally { fullWidth -> -fullWidth } + fadeIn() togetherWith
                            slideOutHorizontally { fullWidth -> fullWidth } + fadeOut()
                }
            }
        ) { targetStep ->
            when (targetStep) {
                CreateMedicationPlanStep.BASIC_INFO -> BasicInfoStep(
                    name = uiState.name,
                    onNameChange = onNameChange,
                    nameError = uiState.nameError,
                    notes = uiState.notes,
                    onNotesChange = onNotesChange
                )

                CreateMedicationPlanStep.SCHEDULE_TYPE -> ScheduleTypeStep(
                    scheduleTypes = uiState.scheduleTypes,
                    selectedScheduleType = uiState.selectedScheduleType,
                    onScheduleTypeChange = onScheduleTypeChange,
                    oneTimeScheduleDate = uiState.oneTimeScheduleDate,
                    onOneTimeScheduleDateChange = onOneTimeScheduleDateChange,
                    startDate = uiState.startDate,
                    onStartDateChange = onStartDateChange,
                    endDate = uiState.endDate,
                    onEndDateChange = onEndDateChange,
                    weeklySelectedDays = uiState.weeklySelectedDays,
                    onWeeklyDaySelected = onWeeklyDaySelected,
                    weekDaysError = uiState.weeklyDaysError,
                    intervalDays = uiState.intervalDays,
                    onIntervalDaysChange = onIntervalDaysChange,
                    intervalDaysError = uiState.intervalDaysError,
                    customCycleDaysOn = uiState.customCycleDaysOn,
                    onCustomCycleDaysOnChange = onCustomCycleDaysOnChange,
                    customCycleDaysOff = uiState.customCycleDaysOff,
                    onCustomCycleDaysOffChange = onCustomCycleDaysOffChange,
                    customCycleDaysError = uiState.customCycleDaysError,
                )

                CreateMedicationPlanStep.DOSAGE -> DosageStep(
                    medications = medications,
                    intakes = uiState.intakes,
                    intakesError = uiState.intakesError,
                    onAddIntakeClick = onAddIntakeClick,
                    onRemoveIntakeClick = onRemoveIntakeClick,
                    onTimeChange = onTimeChange,
                    onAddDoseClick = onAddDoseClick,
                    onRemoveDoseClick = onRemoveDoseClick,
                    onDoseAmountChange = onDoseAmountChange,
                    onDoseMedicationSelected = onDoseMedicationSelected
                )

                CreateMedicationPlanStep.SAVE -> SaveStep(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun CreateMedicationPlanBottomBar(
    uiState: CreateMedicationPlanUiState,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        actions = {
            if (uiState.showPreviousStep) {
                TextButton(onClick = onPreviousClick) {
                    Text(text = stringResource(sharedUiR.string.shared_ui_previous))
                }
            }
        },
        modifier = modifier,
        floatingActionButton = {
            val (textRes, iconRes) = if (uiState.showSaveButton) {
                sharedUiR.string.shared_ui_save to AppIcons.Check
            } else {
                sharedUiR.string.shared_ui_next to AppIcons.ArrowForward
            }
            val text = stringResource(textRes)
            ExtendedFloatingActionButton(
                text = { Text(text = text) },
                icon = { Icon(imageVector = iconRes, contentDescription = text) },
                onClick = onNextClick
            )
        }
    )
}

@Composable
private fun SaveStep(modifier: Modifier = Modifier) {

}

@Preview(showBackground = true)
@Composable
private fun CreateMedicationPlanPreview() {
    val medications = flowOf(PagingData.from(emptyList<Medication>())).collectAsLazyPagingItems()

    AppTheme {
        CreateMedicationPlanScreen(
            uiState = CreateMedicationPlanUiState(),
            medications = medications,
            onBackClick = {},
            onPreviousClick = {},
            onNextClick = {},
            onNameChange = {},
            onNotesChange = {},
            onScheduleTypeChange = {},
            onOneTimeScheduleDateChange = {},
            onStartDateChange = {},
            onEndDateChange = {},
            onWeeklyDaySelected = {},
            onIntervalDaysChange = {},
            onCustomCycleDaysOnChange = {},
            onCustomCycleDaysOffChange = {},
            onAddIntakeClick = {},
            onRemoveIntakeClick = {},
            onTimeChange = { _, _ -> },
            onAddDoseClick = {},
            onRemoveDoseClick = { _, _ -> },
            onDoseAmountChange = { _, _, _ -> },
            onDoseMedicationSelected = { _, _, _ -> },
        )
    }
}