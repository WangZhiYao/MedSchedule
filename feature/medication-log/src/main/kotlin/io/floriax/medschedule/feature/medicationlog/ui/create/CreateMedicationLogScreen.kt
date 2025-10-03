package io.floriax.medschedule.feature.medicationlog.ui.create

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.feature.medicationlog.R
import io.floriax.medschedule.shared.designsystem.icon.AppIcons
import io.floriax.medschedule.shared.designsystem.theme.AppTheme
import io.floriax.medschedule.shared.ui.component.BackButton
import io.floriax.medschedule.shared.ui.component.DatePickerDialog
import io.floriax.medschedule.shared.ui.component.LabeledCheckbox
import io.floriax.medschedule.shared.ui.component.TimePickerDialog
import io.floriax.medschedule.shared.ui.extension.collectSideEffect
import io.floriax.medschedule.shared.ui.extension.collectState
import io.floriax.medschedule.shared.ui.extension.formatLocalized
import io.floriax.medschedule.shared.ui.util.UpToTodaySelectableDates
import java.time.LocalDate
import java.time.LocalTime
import io.floriax.medschedule.shared.ui.R as sharedUiR

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/2
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMedicationLogRoute(
    onBackClick: () -> Unit,
    onAddMedicationClick: () -> Unit,
    viewModel: CreateMedicationLogViewModel = hiltViewModel()
) {

    val state by viewModel.collectState()

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            NavigateToAddMedication -> onAddMedicationClick()
            TakenMedicationsEmpty -> {
                snackbarHostState.showSnackbar(context.getString(R.string.screen_create_medication_log_medication_list_empty))
            }

            is CreateMedicationLogSuccess -> onBackClick()
            CreateMedicationLogFailure -> {
                snackbarHostState.showSnackbar(context.getString(R.string.screen_create_medication_log_create_medication_log_failure))
            }
        }
    }

    if (state.showSelectDateDialog) {
        DatePickerDialog(
            onDismissRequest = { viewModel.toggleDatePickerDialog(false) },
            currentDate = state.selectedDate,
            selectableDates = UpToTodaySelectableDates,
            onDateChange = viewModel::onDateSelected
        )
    }

    if (state.showSelectTimeDialog) {
        TimePickerDialog(
            onDismissRequest = { viewModel.toggleTimePickerDialog(false) },
            currentTime = state.selectedTime,
            onTimeChange = viewModel::onTimeSelected
        )
    }

    if (state.showAddMedicationDialog) {
        AddMedicationDialog(
            onDismissRequest = {
                viewModel.toggleAddMedicationDialog(false)
            },
            onAddMedicationClick = viewModel::confirmAddMedication
        )
    }

    val medicationPageItems = viewModel.pagedMedications.collectAsLazyPagingItems()

    if (state.showSelectMedicationBottomSheet) {
        SelectMedicationBottomSheetDialog(
            medicationPageItems = medicationPageItems,
            selectedMedications = state.takenMedicationInputs.map { takenMedication ->
                takenMedication.medication
            },
            onDismissRequest = {
                viewModel.toggleAddMedicationBottomSheet(false)
            },
            onCheckedChange = { medication, checked ->
                if (checked) {
                    viewModel.onAddTakenMedication(medication)
                } else {
                    viewModel.onRemoveTakenMedication(medication)
                }
            }
        )
    }

    CreateMedicationLogScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onBackClick = onBackClick,
        onSelectDateClick = { viewModel.toggleDatePickerDialog(true) },
        onSelectTimeClick = { viewModel.toggleTimePickerDialog(true) },
        onAddMedicationClick = {
            if (medicationPageItems.itemCount == 0) {
                viewModel.toggleAddMedicationDialog(true)
            } else {
                viewModel.toggleAddMedicationBottomSheet(true)
            }
        },
        onDoseChange = viewModel::onDoseChange,
        onDeductFromStockCheckedChange = viewModel::onDeductFromStockCheckedChange,
        onRemoveTakenMedicationClick = { takenMedication ->
            viewModel.onRemoveTakenMedication(takenMedication.medication)
        },
        onNotesChange = viewModel::onNotesChange,
        onSaveClick = viewModel::attemptCreateMedicationLog
    )
}

@Composable
private fun CreateMedicationLogScreen(
    state: CreateMedicationLogUiState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onSelectDateClick: () -> Unit,
    onSelectTimeClick: () -> Unit,
    onAddMedicationClick: () -> Unit,
    onDoseChange: (Int, String) -> Unit,
    onDeductFromStockCheckedChange: (Int, Boolean) -> Unit,
    onRemoveTakenMedicationClick: (TakenMedicationInput) -> Unit,
    onNotesChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CreateMedicationLogTopBar(onBackClick = onBackClick)
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            dateTimeRow(
                selectedDate = state.selectedDate,
                selectedTime = state.selectedTime,
                onSelectDateClick = onSelectDateClick,
                onSelectTimeClick = onSelectTimeClick
            )

            medicationListHeader()

            if (state.takenMedicationInputs.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.screen_create_medication_log_medication_list_empty),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                itemsIndexed(
                    items = state.takenMedicationInputs,
                    key = { index, takenMedication -> takenMedication.medication.id }
                ) { index, takenMedication ->
                    TakenMedicationCard(
                        takenMedication = takenMedication,
                        onDoseChange = { doseString -> onDoseChange(index, doseString) },
                        onDeductFromStockCheckedChange = { deductFromStock ->
                            onDeductFromStockCheckedChange(index, deductFromStock)
                        },
                        onRemoveClick = { onRemoveTakenMedicationClick(takenMedication) }
                    )
                }
            }

            item {
                FilledTonalButton(
                    onClick = onAddMedicationClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.screen_create_medication_log_select_medication))
                }
            }

            item {
                OutlinedTextField(
                    value = state.notes,
                    onValueChange = onNotesChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = stringResource(R.string.screen_create_medication_log_notes))
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    maxLines = 3,
                    minLines = 3
                )
            }

            item {
                Button(
                    onClick = onSaveClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp)
                ) {
                    Text(text = stringResource(sharedUiR.string.shared_ui_save))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateMedicationLogTopBar(
    onBackClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.screen_create_medication_log_title))
        },
        navigationIcon = {
            BackButton(onClick = onBackClick)
        }
    )
}

private fun LazyListScope.dateTimeRow(
    selectedDate: LocalDate,
    selectedTime: LocalTime,
    onSelectDateClick: () -> Unit,
    onSelectTimeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    item {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = selectedDate.formatLocalized(),
                onValueChange = {},
                modifier = Modifier.weight(1.4f),
                readOnly = true,
                label = {
                    Text(
                        text = stringResource(R.string.screen_create_medication_log_date),
                        modifier = Modifier.basicMarquee()
                    )
                },
                trailingIcon = {
                    IconButton(onClick = onSelectDateClick) {
                        Icon(
                            imageVector = AppIcons.CalendarToday,
                            contentDescription = stringResource(R.string.screen_create_medication_log_select_date)
                        )
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = selectedTime.formatLocalized(),
                onValueChange = {},
                modifier = Modifier.weight(1f),
                readOnly = true,
                label = {
                    Text(
                        text = stringResource(R.string.screen_create_medication_log_time),
                        modifier = Modifier.basicMarquee()
                    )
                },
                trailingIcon = {
                    IconButton(onClick = onSelectTimeClick) {
                        Icon(
                            imageVector = AppIcons.AccessTime,
                            contentDescription = stringResource(R.string.screen_create_medication_log_select_time)
                        )
                    }
                },
                singleLine = true
            )
        }
    }
}

private fun LazyListScope.medicationListHeader(
    modifier: Modifier = Modifier
) {
    item {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.screen_create_medication_log_medication_list_header),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        }
    }
}

@Composable
private fun TakenMedicationCard(
    takenMedication: TakenMedicationInput,
    onDoseChange: (String) -> Unit,
    onDeductFromStockCheckedChange: (Boolean) -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = takenMedication.medication.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .basicMarquee(),
                style = MaterialTheme.typography.titleMedium
            )
            OutlinedTextField(
                value = takenMedication.doseString,
                onValueChange = onDoseChange,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(R.string.screen_create_medication_log_dose))
                },
                suffix = {
                    Text(text = takenMedication.medication.doseUnit)
                },
                supportingText = {
                    val stock = takenMedication.medication.stock
                    val text = if (stock == null) {
                        stringResource(R.string.screen_create_medication_log_medication_stock_not_set)
                    } else {
                        stringResource(
                            R.string.screen_create_medication_log_medication_stock_format,
                            stock.toPlainString()
                        )
                    }
                    Text(text = text)
                },
                isError = takenMedication.isMarkedAsError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                singleLine = true
            )
            Row {
                Box(modifier = Modifier.weight(1f)) {
                    if (takenMedication.deductFromStockEnabled) {
                        LabeledCheckbox(
                            label = {
                                Text(text = stringResource(R.string.screen_create_medication_log_deduct_from_stock))
                            },
                            checked = takenMedication.deductFromStock,
                            onCheckedChange = onDeductFromStockCheckedChange,
                        )
                    }
                }
                TextButton(
                    onClick = onRemoveClick,
                ) {
                    Text(
                        text = stringResource(sharedUiR.string.shared_ui_delete),
                        color = MaterialTheme.colorScheme.error
                    )
                }
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
                Text(text = stringResource(sharedUiR.string.shared_ui_cancel))
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
private fun SelectMedicationBottomSheetDialog(
    medicationPageItems: LazyPagingItems<Medication>,
    selectedMedications: List<Medication>,
    onDismissRequest: () -> Unit,
    onCheckedChange: (Medication, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .navigationBarsPadding()
        ) {
            Text(
                text = stringResource(R.string.screen_create_medication_log_select_medication),
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                items(count = medicationPageItems.itemCount) {
                    val item = medicationPageItems[it]
                    if (item != null) {
                        MedicationItem(
                            medication = item,
                            checked = selectedMedications.contains(item),
                            onCheckedChange = { checked -> onCheckedChange(item, checked) }
                        )
                    }
                }
            }
            TextButton(
                onClick = onDismissRequest,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 16.dp, bottom = 16.dp)
            ) {
                Text(text = stringResource(sharedUiR.string.shared_ui_confirm))
            }
        }
    }
}

@Composable
private fun MedicationItem(
    medication: Medication,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = {
            Text(
                text = medication.name,
                modifier = Modifier.basicMarquee(),
                style = MaterialTheme.typography.titleMedium
            )
        },
        modifier = modifier.toggleable(
            value = checked,
            role = Role.Checkbox,
            onValueChange = { onCheckedChange(!checked) },
        ),
        supportingContent = {
            val stock = medication.stock
            val text = if (stock == null) {
                stringResource(R.string.screen_create_medication_log_medication_stock_not_set)
            } else {
                stringResource(
                    R.string.screen_create_medication_log_medication_stock_format,
                    stock.toPlainString()
                )
            }
            Text(text = text)
        },
        leadingContent = {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
            )
        },
        trailingContent = {
            Text(text = medication.doseUnit, style = MaterialTheme.typography.bodyLarge)
        },
        colors = ListItemDefaults.colors(MaterialTheme.colorScheme.surfaceContainerLow)
    )
}

@Preview(showBackground = true)
@Composable
private fun MedicationItemPreview() {
    AppTheme {
        MedicationItem(
            medication = Medication(
                name = "Aspirin",
                stock = 10.toBigDecimal(),
                doseUnit = "mg",
                notes = "Notes"
            ),
            checked = true,
            onCheckedChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TakenMedicationItemPreview() {
    AppTheme {
        TakenMedicationCard(
            takenMedication = TakenMedicationInput(
                medication = Medication(
                    name = "Aspirin",
                    stock = 10.toBigDecimal(),
                    doseUnit = "mg",
                    notes = "Notes"
                ),
                doseString = "1",
                isMarkedAsError = false
            ),
            onDoseChange = {},
            onDeductFromStockCheckedChange = {},
            onRemoveClick = {},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun CreateMedicationLogScreenPreview() {
    AppTheme {
        CreateMedicationLogScreen(
            state = CreateMedicationLogUiState(),
            snackbarHostState = remember { SnackbarHostState() },
            onBackClick = {},
            onSelectDateClick = {},
            onSelectTimeClick = {},
            onAddMedicationClick = {},
            onDoseChange = { index, doseString -> },
            onDeductFromStockCheckedChange = { index, deductFromStock -> },
            onRemoveTakenMedicationClick = {},
            onNotesChange = {},
            onSaveClick = {}
        )
    }
}