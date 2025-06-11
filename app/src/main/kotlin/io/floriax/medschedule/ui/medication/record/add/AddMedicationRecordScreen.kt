package io.floriax.medschedule.ui.medication.record.add

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import io.floriax.medschedule.R
import io.floriax.medschedule.common.ext.collectSideEffect
import io.floriax.medschedule.common.ext.collectState
import io.floriax.medschedule.common.ext.formatDate
import io.floriax.medschedule.common.ext.formatTime
import io.floriax.medschedule.common.ext.toLocalDateFromUtc
import io.floriax.medschedule.common.ext.toUtcStartOfDayMillis
import io.floriax.medschedule.domain.model.Medication
import io.floriax.medschedule.ui.designsystem.AppIcons
import io.floriax.medschedule.ui.theme.AppTheme
import java.time.LocalDate
import java.time.LocalTime

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
@Composable
fun AddMedicationRecordRoute(
    onBackClick: () -> Unit,
    onAddMedicationClick: () -> Unit,
    onMedicationRecordAdded: () -> Unit,
    viewModel: AddMedicationRecordViewModel = hiltViewModel(),
) {

    val state by viewModel.collectState()

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    var showAddMedicationDialog by remember { mutableStateOf(false) }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            EmptyMedication -> {
                showAddMedicationDialog = true
            }

            AddMedicationRecordSuccess -> {
                onMedicationRecordAdded()
            }

            AddMedicationRecordFailed -> {
                snackbarHostState.showSnackbar(context.getString(R.string.error_add_medication_record_failed))
            }
        }
    }

    if (showAddMedicationDialog) {
        AddMedicationDialog(
            onDismissRequest = { showAddMedicationDialog = false },
            onAddMedicationClick = {
                showAddMedicationDialog = false
                onAddMedicationClick()
            }
        )
    }

    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        MedicationRecordDatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            currentDate = state.date,
            onDateChange = viewModel::onDateChange
        )
    }

    var showTimePicker by remember { mutableStateOf(false) }

    if (showTimePicker) {
        MedicationRecordTimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            currentTime = state.time,
            onTimeChange = viewModel::onTimeChange
        )
    }

    AddMedicationRecordScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onBackClick = onBackClick,
        onSelectDateClick = { showDatePicker = true },
        onSelectTimeClick = { showTimePicker = true },
        onDeleteTakenMedicationItemClick = viewModel::onDeleteTakenMedicationClick,
        onAddTakenMedicationItemClick = viewModel::onAddTakenMedicationClick,
        onTakenMedicationItemContentChange = viewModel::onTakenMedicationItemContentChange,
        onRemarkChange = viewModel::onRemarkChange,
        onAddMedicationRecordClick = viewModel::attemptAddMedicationRecord
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddMedicationRecordScreen(
    state: AddMedicationRecordViewState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onSelectDateClick: () -> Unit,
    onSelectTimeClick: () -> Unit,
    onDeleteTakenMedicationItemClick: (Int) -> Unit,
    onAddTakenMedicationItemClick: () -> Unit,
    onTakenMedicationItemContentChange: (Int, TakenMedicationItem) -> Unit,
    onRemarkChange: (String) -> Unit,
    onAddMedicationRecordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AddMedicationRecordTopBar(onBackClick = onBackClick)
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            item {
                DateTimeRow(
                    date = state.date,
                    time = state.time,
                    onSelectDateClick = onSelectDateClick,
                    onSelectTimeClick = onSelectTimeClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.add_medication_record_medication_and_dose),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            itemsIndexed(state.takenMedications) { index, item ->
                Spacer(modifier = Modifier.height(8.dp))
                TakenMedicationCard(
                    medications = state.medications,
                    onDeleteClick = { onDeleteTakenMedicationItemClick(index) },
                    index = index,
                    item = item,
                    onTakenMedicationItemContentChange = { newItem ->
                        onTakenMedicationItemContentChange(index, newItem)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem(),
                    deleteEnabled = state.takenMedications.size > 1 && index > 0
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                ElevatedButton(
                    onClick = onAddTakenMedicationItemClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.add_medication_record_add_medication_and_dose))
                }
            }

            item {
                OutlinedTextField(
                    value = state.remark,
                    onValueChange = onRemarkChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = stringResource(R.string.edit_medication_remark)) },
                    trailingIcon = {
                        if (state.remark.isNotEmpty()) {
                            IconButton(onClick = { onRemarkChange("") }) {
                                Icon(
                                    imageVector = AppIcons.Clear,
                                    contentDescription = stringResource(R.string.clear)
                                )
                            }
                        }
                    },
                    maxLines = 3,
                    minLines = 3
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onAddMedicationRecordClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.save))
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddMedicationRecordTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.add_medication_record_title))
        },
        modifier = modifier.fillMaxWidth(),
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = AppIcons.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        }
    )
}

@Composable
private fun DateTimeRow(
    date: LocalDate,
    time: LocalTime,
    onSelectDateClick: () -> Unit,
    onSelectTimeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ClickableRow(
            label = stringResource(R.string.add_medication_record_date),
            value = date.formatDate(),
            icon = AppIcons.Calendar,
            onClick = onSelectDateClick,
            modifier = Modifier.weight(1.2f)
        )
        ClickableRow(
            label = stringResource(R.string.add_medication_record_time),
            value = time.formatTime(),
            icon = AppIcons.Clock,
            onClick = onSelectTimeClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ClickableRow(
    label: String,
    value: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = label, style = MaterialTheme.typography.labelMedium)
                Text(text = value, style = MaterialTheme.typography.bodyLarge)
            }
            Icon(imageVector = icon, contentDescription = null)
        }
    }
}

@Composable
private fun TakenMedicationCard(
    medications: List<Medication>,
    onDeleteClick: () -> Unit,
    index: Int,
    item: TakenMedicationItem,
    onTakenMedicationItemContentChange: (TakenMedicationItem) -> Unit,
    modifier: Modifier = Modifier,
    deleteEnabled: Boolean = true,
) {
    OutlinedCard(
        modifier = modifier,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = stringResource(
                        R.string.add_medication_record_medication_index,
                        index + 1
                    ),
                    style = MaterialTheme.typography.titleSmall
                )

                TextButton(
                    onClick = onDeleteClick,
                    enabled = deleteEnabled
                ) {
                    Text(text = stringResource(R.string.delete))
                }
            }

            MedicationDropdownMenu(
                medications = medications,
                selectedMedication = item.selectedMedication,
                medicationError = item.medicationError,
                onMedicationSelect = { medication ->
                    onTakenMedicationItemContentChange(item.copy(selectedMedication = medication))
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = item.doseString,
                onValueChange = { doseString ->
                    onTakenMedicationItemContentChange(item.copy(doseString = doseString))
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.add_medication_record_dose)) },
                suffix = {
                    if (item.selectedMedication != null) {
                        Text(text = item.selectedMedication.doseUnit)
                    }
                },
                supportingText = {
                    Text(text = if (item.doseError) stringResource(R.string.error_medication_dose_invalid) else "")
                },
                isError = item.doseError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MedicationDropdownMenu(
    medications: List<Medication>,
    selectedMedication: Medication?,
    medicationError: MedicationError?,
    onMedicationSelect: (Medication) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { value -> if (medications.isNotEmpty()) expanded = value },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedMedication?.name ?: "",
            onValueChange = {},
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
            readOnly = true,
            label = {
                Text(
                    text = stringResource(R.string.add_medication_record_medication),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            supportingText = {
                val text = when (medicationError) {
                    NotSelected -> stringResource(R.string.error_medication_not_selected)
                    Duplicated -> stringResource(R.string.error_medication_duplicated)
                    null -> ""
                }
                Text(text = text)
            },
            isError = medicationError != null,
            singleLine = true
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            medications.forEach { medication ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(
                                R.string.medication_name_dose_unit,
                                medication.name,
                                medication.doseUnit
                            )
                        )
                    },
                    onClick = {
                        onMedicationSelect(medication)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun AddMedicationDialog(
    onDismissRequest: () -> Unit,
    onAddMedicationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onAddMedicationClick) {
                Text(text = stringResource(R.string.dialog_add_medication_confirm))
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.cancel))
            }
        },
        title = { Text(text = stringResource(R.string.dialog_add_medication_title)) },
        text = {
            Text(text = stringResource(R.string.dialog_add_medication_content))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MedicationRecordDatePickerDialog(
    onDismissRequest: () -> Unit,
    currentDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberDatePickerState(
        initialSelectedDateMillis = currentDate.toUtcStartOfDayMillis()
    )

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                    state.selectedDateMillis?.let { selectedDateMillis ->
                        onDateChange(selectedDateMillis.toLocalDateFromUtc())
                    }
                }
            ) {
                Text(text = stringResource(R.string.confirm))
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(state = state)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MedicationRecordTimePickerDialog(
    onDismissRequest: () -> Unit,
    currentTime: LocalTime,
    onTimeChange: (LocalTime) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberTimePickerState(
        initialHour = currentTime.hour,
        initialMinute = currentTime.minute
    )

    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier.wrapContentWidth(),
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min),
            shape = DatePickerDefaults.shape,
            color = DatePickerDefaults.colors().containerColor,
            tonalElevation = DatePickerDefaults.TonalElevation,
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.dialog_time_picker_title),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelLarge
                )
                TimePicker(state = state)
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = onDismissRequest) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    TextButton(
                        onClick = {
                            onDismissRequest()
                            onTimeChange(LocalTime.of(state.hour, state.minute))
                        }
                    ) {
                        Text(text = stringResource(R.string.confirm))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddMedicationRecordScreenPreview() {
    AppTheme {
        AddMedicationRecordScreen(
            state = AddMedicationRecordViewState(),
            snackbarHostState = SnackbarHostState(),
            onBackClick = {},
            onSelectDateClick = {},
            onSelectTimeClick = {},
            onDeleteTakenMedicationItemClick = {},
            onAddTakenMedicationItemClick = {},
            onTakenMedicationItemContentChange = { _, _ -> },
            onRemarkChange = {},
            onAddMedicationRecordClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddMedicationDialogPreview() {
    AppTheme {
        AddMedicationDialog(
            onDismissRequest = {},
            onAddMedicationClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MedicationRecordDatePickerDialogPreview() {
    AppTheme {
        MedicationRecordDatePickerDialog(
            onDismissRequest = {},
            currentDate = LocalDate.now(),
            onDateChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MedicationRecordTimePickerDialogPreview() {
    AppTheme {
        MedicationRecordTimePickerDialog(
            onDismissRequest = {},
            currentTime = LocalTime.now(),
            onTimeChange = {}
        )
    }
}
