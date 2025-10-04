package io.floriax.medschedule.shared.ui.component

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.floriax.medschedule.shared.designsystem.icon.AppIcons
import io.floriax.medschedule.shared.ui.R
import io.floriax.medschedule.shared.ui.extension.formatLocalized
import io.floriax.medschedule.shared.ui.util.FromTodaySelectableDates
import java.time.LocalDate

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/10/4
 */
@Composable
fun DateTextField(
    value: LocalDate?,
    onValueChange: (LocalDate) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    selectableDates: SelectableDates = FromTodaySelectableDates,
    onClear: (() -> Unit)? = null,
) {
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            currentDate = value ?: LocalDate.now(),
            selectableDates = selectableDates,
            onDateChange = {
                onValueChange(it)
                showDatePicker = false
            }
        )
    }

    OutlinedTextField(
        value = value?.formatLocalized() ?: "",
        onValueChange = {},
        modifier = modifier,
        readOnly = true,
        label = { Text(text = label) },
        trailingIcon = {
            if (value != null && onClear != null) {
                IconButton(onClick = onClear) {
                    Icon(
                        imageVector = AppIcons.RemoveCircle,
                        contentDescription = stringResource(R.string.shared_ui_remove)
                    )
                }
            } else {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(
                        imageVector = AppIcons.CalendarToday,
                        contentDescription = label
                    )
                }
            }
        }
    )
}
