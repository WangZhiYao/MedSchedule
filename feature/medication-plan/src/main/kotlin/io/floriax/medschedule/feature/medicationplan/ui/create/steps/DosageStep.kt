package io.floriax.medschedule.feature.medicationplan.ui.create.steps

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.feature.medicationplan.R
import io.floriax.medschedule.feature.medicationplan.ui.create.DoseError
import io.floriax.medschedule.feature.medicationplan.ui.create.DoseInput
import io.floriax.medschedule.feature.medicationplan.ui.create.DosesError
import io.floriax.medschedule.feature.medicationplan.ui.create.IntakeInput
import io.floriax.medschedule.feature.medicationplan.ui.create.IntakesError
import io.floriax.medschedule.feature.medicationplan.ui.create.TimeError
import io.floriax.medschedule.shared.designsystem.icon.AppIcons
import io.floriax.medschedule.shared.designsystem.theme.AppTheme
import io.floriax.medschedule.shared.ui.component.TimePickerDialog
import io.floriax.medschedule.shared.ui.extension.formatLocalized
import kotlinx.coroutines.flow.flowOf
import java.math.BigDecimal
import java.time.LocalTime

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/10/4
 */
@Composable
fun DosageStep(
    medications: LazyPagingItems<Medication>,
    intakes: List<IntakeInput>,
    intakesError: IntakesError?,
    onAddIntakeClick: () -> Unit,
    onRemoveIntakeClick: (IntakeInput) -> Unit,
    onTimeChange: (String, LocalTime) -> Unit,
    onRemoveDoseClick: (String, String) -> Unit,
    onDoseAmountChange: (String, String, String) -> Unit,
    onMedicationSelectionChanged: (String, Medication, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var editingIntakeId by remember { mutableStateOf<String?>(null) }

    val editingIntake = editingIntakeId?.let { id -> intakes.find { it.id == id } }

    editingIntake?.let { intake ->
        SelectMedicationBottomSheet(
            medications = medications,
            currentDoses = intake.doses,
            onDismissRequest = { editingIntakeId = null },
            onMedicationCheckedChange = { medication, isChecked ->
                onMedicationSelectionChanged(intake.id, medication, isChecked)
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.screen_create_medication_plan_dosage_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            if (intakesError is IntakesError.Empty) {
                Text(
                    text = stringResource(R.string.screen_create_medication_plan_error_intakes_empty),
                    modifier = Modifier.padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        items(items = intakes, key = { intake -> intake.id }) { intake ->
            IntakeBlock(
                intake = intake,
                onRemoveIntakeClick = { onRemoveIntakeClick(intake) },
                onTimeChange = { newTime -> onTimeChange(intake.id, newTime) },
                onSelectMedicationsClick = { editingIntakeId = intake.id },
                onRemoveDoseClick = { dose -> onRemoveDoseClick(intake.id, dose.id) },
                onDoseAmountChange = { dose, amount ->
                    onDoseAmountChange(
                        intake.id,
                        dose.id,
                        amount
                    )
                }
            )
        }

        item {
            Button(
                onClick = onAddIntakeClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                val text =
                    stringResource(R.string.screen_create_medication_plan_dosage_add_intake_time)
                Icon(imageVector = AppIcons.AccessAlarm, contentDescription = text)
                Text(text = text)
            }
        }
    }
}

@Composable
private fun IntakeBlock(
    intake: IntakeInput,
    onRemoveIntakeClick: () -> Unit,
    onTimeChange: (LocalTime) -> Unit,
    onSelectMedicationsClick: () -> Unit,
    onRemoveDoseClick: (DoseInput) -> Unit,
    onDoseAmountChange: (DoseInput, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showTimePicker by remember { mutableStateOf(false) }

    if (showTimePicker) {
        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            currentTime = intake.time,
            onTimeChange = {
                onTimeChange(it)
                showTimePicker = false
            }
        )
    }

    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = AppIcons.AccessTime,
                    contentDescription = stringResource(R.string.screen_create_medication_plan_dosage_intake_time)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = intake.time.formatLocalized(),
                        style = MaterialTheme.typography.titleMedium,
                        color = if (intake.timeError != null) MaterialTheme.colorScheme.error else Color.Unspecified
                    )
                    if (intake.timeError is TimeError.Duplicate) {
                        Text(
                            text = stringResource(R.string.screen_create_medication_plan_error_intake_time_duplicate),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                IconButton(onClick = { showTimePicker = true }) {
                    Icon(
                        imageVector = AppIcons.Edit,
                        contentDescription = stringResource(R.string.screen_create_medication_plan_dosage_edit_intake_time)
                    )
                }
                IconButton(onClick = onRemoveIntakeClick) {
                    Icon(
                        imageVector = AppIcons.Delete,
                        contentDescription = stringResource(R.string.screen_create_medication_plan_dosage_remove_intake_time),
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (intake.dosesError is DosesError.Empty) {
                    Text(
                        text = stringResource(R.string.screen_create_medication_plan_error_doses_empty),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                intake.doses.forEach { dose ->
                    DoseItem(
                        dose = dose,
                        onRemoveClick = { onRemoveDoseClick(dose) },
                        onAmountChange = { amount -> onDoseAmountChange(dose, amount) }
                    )
                }
                val label =
                    stringResource(R.string.screen_create_medication_plan_dosage_add_medication)
                FilledTonalButton(
                    onClick = onSelectMedicationsClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = AppIcons.Add, contentDescription = label)
                    Text(text = label)
                }
            }
        }
    }
}

@Composable
private fun DoseItem(
    dose: DoseInput,
    onRemoveClick: () -> Unit,
    onAmountChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = AppIcons.MedicationBorder,
                    contentDescription = stringResource(R.string.screen_create_medication_plan_dosage_medication)
                )
                Text(
                    text = dose.medication?.name ?: "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .basicMarquee(),
                    style = MaterialTheme.typography.titleSmall
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = dose.dose,
                    onValueChange = onAmountChange,
                    modifier = Modifier.weight(1f),
                    label = { Text(text = stringResource(R.string.screen_create_medication_plan_dosage_dose)) },
                    suffix = { Text(text = dose.medication?.doseUnit ?: "") },
                    supportingText = {
                        if (dose.doseError != null) {
                            val text = when (dose.doseError) {
                                DoseError.Empty -> stringResource(R.string.screen_create_medication_plan_error_dose_empty)
                                DoseError.Invalid -> stringResource(R.string.screen_create_medication_plan_error_dose_invalid)
                            }
                            Text(text = text)
                        }
                    },
                    isError = dose.doseError != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                IconButton(onClick = onRemoveClick) {
                    Icon(
                        imageVector = AppIcons.Close,
                        contentDescription = stringResource(R.string.screen_create_medication_plan_dosage_remove_medication),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectMedicationBottomSheet(
    medications: LazyPagingItems<Medication>,
    currentDoses: List<DoseInput>,
    onDismissRequest: () -> Unit,
    onMedicationCheckedChange: (Medication, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var searchQuery by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .navigationBarsPadding()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                label = { Text(stringResource(R.string.screen_create_medication_plan_dosage_search_medications)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                items(
                    count = medications.itemCount,
                    key = medications.itemKey { it.id }
                ) { index ->
                    val medication = medications[index]
                    if (medication != null) {
                        if (searchQuery.isBlank() || medication.name.contains(
                                searchQuery,
                                ignoreCase = true
                            )
                        ) {
                            val isChecked = currentDoses.any { it.medication?.id == medication.id }
                            MedicationSelectionItem(
                                medication = medication,
                                checked = isChecked,
                                onCheckedChange = { onMedicationCheckedChange(medication, it) }
                            )
                        }
                    }
                }
            }
            TextButton(
                onClick = onDismissRequest,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(16.dp)
            ) {
                Text(text = stringResource(io.floriax.medschedule.shared.ui.R.string.shared_ui_confirm))
            }
        }
    }
}

@Composable
private fun MedicationSelectionItem(
    medication: Medication,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = { Text(medication.name) },
        modifier = modifier.toggleable(
            value = checked,
            role = Role.Checkbox,
            onValueChange = onCheckedChange
        ),
        leadingContent = {
            Checkbox(
                checked = checked,
                onCheckedChange = null
            )
        },
        supportingContent = {
            val stock = medication.stock
            val text = if (stock == null) {
                stringResource(R.string.screen_create_medication_plan_dosage_stock_not_set)
            } else {
                stringResource(
                    R.string.screen_create_medication_plan_dosage_stock_format,
                    stock.toPlainString()
                )
            }
            Text(text = text)
        },
        trailingContent = { Text(text = medication.doseUnit) },
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    )
}

@Preview(showBackground = true)
@Composable
private fun DosageStepPreview() {
    val sampleMedication1 = remember {
        Medication(
            id = 1,
            name = "复方氨酚烷胺片",
            stock = BigDecimal(10),
            doseUnit = "片",
            notes = ""
        )
    }
    val sampleMedication2 = remember {
        Medication(
            id = 2,
            name = "布洛芬缓释胶囊",
            stock = BigDecimal(20),
            doseUnit = "粒",
            notes = ""
        )
    }

    val sampleIntakes = remember {
        listOf(
            IntakeInput(
                time = LocalTime.of(8, 0),
                doses = listOf(
                    DoseInput(medication = sampleMedication1, dose = "1"),
                    DoseInput(medication = sampleMedication2, dose = "2"),
                )
            ),
            IntakeInput(
                time = LocalTime.of(20, 30),
                doses = listOf(
                    DoseInput(medication = null, dose = "", doseError = DoseError.Empty)
                )
            )
        )
    }

    val medications =
        flowOf(
            PagingData.from(
                listOf(
                    sampleMedication1,
                    sampleMedication2
                )
            )
        ).collectAsLazyPagingItems()

    AppTheme {
        DosageStep(
            medications = medications,
            intakes = sampleIntakes,
            intakesError = null,
            onAddIntakeClick = {},
            onRemoveIntakeClick = {},
            onTimeChange = { _, _ -> },
            onRemoveDoseClick = { _, _ -> },
            onDoseAmountChange = { _, _, _ -> },
            onMedicationSelectionChanged = { _, _, _ -> }
        )
    }
}
