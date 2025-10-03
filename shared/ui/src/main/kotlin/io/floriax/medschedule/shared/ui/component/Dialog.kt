package io.floriax.medschedule.shared.ui.component

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.floriax.medschedule.shared.ui.R
import io.floriax.medschedule.shared.ui.extension.toLocalDateAtZone
import io.floriax.medschedule.shared.ui.extension.toStartOfDayAtUtc
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/3
 */
@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    currentDate: LocalDate,
    selectableDates: SelectableDates,
    onDateChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberDatePickerState(
        initialSelectedDateMillis = currentDate.toStartOfDayAtUtc().toEpochMilli(),
        selectableDates = selectableDates
    )

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                    state.selectedDateMillis?.let { selectedDateMillis ->
                        onDateChange(selectedDateMillis.toLocalDateAtZone(ZoneOffset.UTC))
                    }
                }
            ) {
                Text(text = stringResource(R.string.shared_ui_confirm))
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.shared_ui_cancel))
            }
        }
    ) {
        DatePicker(state = state)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    currentTime: LocalTime,
    onTimeChange: (LocalTime) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberTimePickerState(
        initialHour = currentTime.hour,
        initialMinute = currentTime.minute
    )

    TimePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                    onTimeChange(LocalTime.of(state.hour, state.minute))
                }
            ) {
                Text(text = stringResource(R.string.shared_ui_confirm))
            }
        },
        title = {
            Text(text = stringResource(R.string.shared_ui_dialog_time_picker))
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.shared_ui_cancel))
            }
        }
    ) {
        TimePicker(state = state)
    }
}