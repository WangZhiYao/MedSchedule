package io.floriax.medschedule.feature.medicationplan.ui.create.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.floriax.medschedule.core.domain.enums.MedicationScheduleType
import io.floriax.medschedule.feature.medicationplan.R
import io.floriax.medschedule.feature.medicationplan.ui.create.CustomCycleDaysError
import io.floriax.medschedule.feature.medicationplan.ui.create.IntervalDaysError
import io.floriax.medschedule.feature.medicationplan.ui.create.WeekDaysError
import io.floriax.medschedule.shared.designsystem.theme.AppTheme
import io.floriax.medschedule.shared.ui.component.DateTextField
import io.floriax.medschedule.shared.ui.component.WeekDaySelector
import io.floriax.medschedule.shared.ui.extension.label
import io.floriax.medschedule.shared.ui.util.AfterSpecialDateSelectableDates
import io.floriax.medschedule.shared.ui.util.FromTodaySelectableDates
import java.time.DayOfWeek
import java.time.LocalDate

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/10/3
 */
@Composable
fun ScheduleTypeStep(
    scheduleTypes: List<MedicationScheduleType>,
    selectedScheduleType: MedicationScheduleType,
    onScheduleTypeChange: (MedicationScheduleType) -> Unit,
    oneTimeScheduleDate: LocalDate,
    onOneTimeScheduleDateChange: (LocalDate) -> Unit,
    startDate: LocalDate,
    onStartDateChange: (LocalDate) -> Unit,
    endDate: LocalDate?,
    onEndDateChange: (LocalDate?) -> Unit,
    weeklySelectedDays: Set<DayOfWeek>,
    onWeeklyDaySelected: (DayOfWeek) -> Unit,
    weekDaysError: WeekDaysError?,
    intervalDays: String,
    onIntervalDaysChange: (String) -> Unit,
    intervalDaysError: IntervalDaysError?,
    customCycleDaysOn: String,
    onCustomCycleDaysOnChange: (String) -> Unit,
    customCycleDaysOff: String,
    onCustomCycleDaysOffChange: (String) -> Unit,
    customCycleDaysError: CustomCycleDaysError?,
    modifier: Modifier = Modifier
) {

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.screen_create_medication_plan_medication_schedule_type_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        ScheduleTypeDropDownMenu(
            scheduleTypes = scheduleTypes,
            selectedScheduleType = selectedScheduleType,
            onScheduleTypeChange = onScheduleTypeChange,
            modifier = Modifier.fillMaxWidth()
        )

        if (selectedScheduleType == MedicationScheduleType.ONE_TIME) {
            val label = stringResource(R.string.screen_create_medication_plan_schedule_date_title)
            DateTextField(
                value = oneTimeScheduleDate,
                onValueChange = onOneTimeScheduleDateChange,
                label = label,
                selectableDates = FromTodaySelectableDates,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            ScheduleDateRange(
                startDate = startDate,
                onStartDateChange = onStartDateChange,
                endDate = endDate,
                onEndDateChange = onEndDateChange,
            )

            if (selectedScheduleType == MedicationScheduleType.WEEKLY) {
                WeeklyScheduleOptions(
                    selectedDays = weeklySelectedDays,
                    onDayClick = onWeeklyDaySelected,
                    weekDaysError = weekDaysError
                )
            }

            if (selectedScheduleType == MedicationScheduleType.INTERVAL) {
                IntervalScheduleOptions(
                    intervalDays = intervalDays,
                    onIntervalDaysChange = onIntervalDaysChange,
                    intervalDaysError = intervalDaysError
                )
            }

            if (selectedScheduleType == MedicationScheduleType.CUSTOM_CYCLE) {
                CustomCycleScheduleOptions(
                    daysOn = customCycleDaysOn,
                    onDaysOnChange = onCustomCycleDaysOnChange,
                    daysOff = customCycleDaysOff,
                    onDaysOffChange = onCustomCycleDaysOffChange,
                    customCycleDaysError = customCycleDaysError
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleTypeDropDownMenu(
    scheduleTypes: List<MedicationScheduleType>,
    selectedScheduleType: MedicationScheduleType,
    onScheduleTypeChange: (MedicationScheduleType) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedScheduleType.label(),
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            readOnly = true,
            label = {
                Text(text = stringResource(R.string.screen_create_medication_plan_schedule_type_title))
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            singleLine = true,
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            scheduleTypes.forEach { type ->
                DropdownMenuItem(
                    text = {
                        Text(
                            type.label(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    onClick = {
                        expanded = false
                        onScheduleTypeChange(type)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Composable
private fun ScheduleDateRange(
    startDate: LocalDate,
    onStartDateChange: (LocalDate) -> Unit,
    endDate: LocalDate?,
    onEndDateChange: (LocalDate?) -> Unit,
    modifier: Modifier = Modifier
) {
    val startDateLabel = stringResource(R.string.screen_create_medication_plan_start_date)
    val endDateLabel = stringResource(R.string.screen_create_medication_plan_end_date)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DateTextField(
            value = startDate,
            onValueChange = onStartDateChange,
            label = startDateLabel,
            modifier = Modifier.fillMaxWidth(),
        )

        DateTextField(
            value = endDate,
            onValueChange = { onEndDateChange(it) },
            label = endDateLabel,
            modifier = Modifier.fillMaxWidth(),
            selectableDates = AfterSpecialDateSelectableDates(maxOf(startDate, LocalDate.now())),
            onClear = { onEndDateChange(null) },
        )
    }
}

@Composable
private fun WeeklyScheduleOptions(
    selectedDays: Set<DayOfWeek>,
    onDayClick: (DayOfWeek) -> Unit,
    weekDaysError: WeekDaysError?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.screen_create_medication_plan_weekly_title),
            style = MaterialTheme.typography.titleMedium,
        )
        WeekDaySelector(
            selectedDays = selectedDays,
            onDayClick = onDayClick,
            isError = weekDaysError != null
        )
        if (weekDaysError != null) {
            val message = when (weekDaysError) {
                WeekDaysError.Empty -> stringResource(R.string.screen_create_medication_plan_error_weekly_days_empty)
            }
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
private fun IntervalScheduleOptions(
    intervalDays: String,
    onIntervalDaysChange: (String) -> Unit,
    intervalDaysError: IntervalDaysError?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.screen_create_medication_plan_interval_setting_title),
            style = MaterialTheme.typography.titleMedium
        )
        OutlinedTextField(
            value = intervalDays,
            onValueChange = onIntervalDaysChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            label = {
                Text(text = stringResource(R.string.screen_create_medication_plan_interval_days_title))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            isError = intervalDaysError != null,
            supportingText = {
                if (intervalDaysError != null) {
                    val message = when (intervalDaysError) {
                        IntervalDaysError.Empty -> stringResource(R.string.screen_create_medication_plan_error_interval_days_empty)
                        IntervalDaysError.Invalid -> stringResource(R.string.screen_create_medication_plan_error_interval_days_invalid)
                    }
                    Text(text = message)
                }
            }
        )
    }
}

@Composable
private fun CustomCycleScheduleOptions(
    daysOn: String,
    onDaysOnChange: (String) -> Unit,
    daysOff: String,
    onDaysOffChange: (String) -> Unit,
    customCycleDaysError: CustomCycleDaysError?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.screen_create_medication_plan_custom_cycle_setting_title),
            style = MaterialTheme.typography.titleMedium,
        )
        Row(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = daysOn,
                onValueChange = onDaysOnChange,
                modifier = Modifier.weight(1f),
                label = { Text(text = stringResource(R.string.screen_create_medication_plan_custom_cycle_days_on_title)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                isError = customCycleDaysError is CustomCycleDaysError.DaysOnEmpty
            )
            OutlinedTextField(
                value = daysOff,
                onValueChange = onDaysOffChange,
                modifier = Modifier.weight(1f),
                label = { Text(text = stringResource(R.string.screen_create_medication_plan_custom_cycle_days_off_title)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = customCycleDaysError is CustomCycleDaysError.DaysOffEmpty
            )
        }
        if (customCycleDaysError != null) {
            val message = when (customCycleDaysError) {
                CustomCycleDaysError.DaysOnEmpty -> stringResource(R.string.screen_create_medication_plan_error_custom_cycle_days_on_empty)
                CustomCycleDaysError.DaysOffEmpty -> stringResource(R.string.screen_create_medication_plan_error_custom_cycle_days_off_empty)
                CustomCycleDaysError.Invalid -> stringResource(R.string.screen_create_medication_plan_error_custom_cycle_days_invalid)
            }
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScheduleTypeStepPreview() {
    AppTheme {
        ScheduleTypeStep(
            scheduleTypes = MedicationScheduleType.entries.filterNot { type -> type == MedicationScheduleType.UNKNOWN },
            selectedScheduleType = MedicationScheduleType.CUSTOM_CYCLE,
            onScheduleTypeChange = {},
            oneTimeScheduleDate = LocalDate.now(),
            onOneTimeScheduleDateChange = {},
            startDate = LocalDate.now(),
            onStartDateChange = {},
            endDate = null,
            onEndDateChange = {},
            weeklySelectedDays = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
            onWeeklyDaySelected = {},
            weekDaysError = null,
            intervalDays = "",
            onIntervalDaysChange = {},
            intervalDaysError = null,
            customCycleDaysOn = "21",
            onCustomCycleDaysOnChange = {},
            customCycleDaysOff = "7",
            onCustomCycleDaysOffChange = {},
            customCycleDaysError = null,
        )
    }
}
