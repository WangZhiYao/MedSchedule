package io.floriax.medschedule.feature.medicationplan.ui.create.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
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
    onTimeChange: (IntakeInput, LocalTime) -> Unit,
    onAddDoseClick: (IntakeInput) -> Unit,
    onRemoveDoseClick: (IntakeInput, DoseInput) -> Unit,
    onDoseAmountChange: (IntakeInput, DoseInput, String) -> Unit,
    onDoseMedicationSelected: (IntakeInput, DoseInput, Medication) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
    ) {
        item {
            Text(
                text = stringResource(R.string.screen_create_medication_plan_dosage_title),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            if (intakesError is IntakesError.Empty) {
                Text(
                    text = stringResource(R.string.screen_create_medication_plan_error_intakes_empty),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        itemsIndexed(items = intakes, key = { _, intake -> intake.id }) { index, intake ->
            IntakeBlock(
                medications = medications,
                intake = intake,
                onRemoveIntakeClick = { onRemoveIntakeClick(intake) },
                onTimeChange = { newTime -> onTimeChange(intake, newTime) },
                onAddDoseClick = { onAddDoseClick(intake) },
                onRemoveDoseClick = { dose -> onRemoveDoseClick(intake, dose) },
                onDoseAmountChange = { dose, amount -> onDoseAmountChange(intake, dose, amount) },
                onDoseMedicationSelected = { dose, medication ->
                    onDoseMedicationSelected(
                        intake,
                        dose,
                        medication
                    )
                }
            )
            if (index < intakes.size - 1) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onAddIntakeClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = AppIcons.Add, contentDescription = null)
                Text(text = stringResource(R.string.screen_create_medication_plan_dosage_add_intake_time))
            }
        }
    }
}

@Composable
private fun IntakeBlock(
    medications: LazyPagingItems<Medication>,
    intake: IntakeInput,
    onRemoveIntakeClick: () -> Unit,
    onTimeChange: (LocalTime) -> Unit,
    onAddDoseClick: () -> Unit,
    onRemoveDoseClick: (DoseInput) -> Unit,
    onDoseAmountChange: (DoseInput, String) -> Unit,
    onDoseMedicationSelected: (DoseInput, Medication) -> Unit,
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

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { showTimePicker = true },
            ) {
                Icon(imageVector = AppIcons.CalendarClock, contentDescription = null)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = intake.time.formatLocalized(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
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
            IconButton(onClick = onRemoveIntakeClick) {
                Icon(
                    imageVector = AppIcons.Delete,
                    contentDescription = stringResource(R.string.screen_create_medication_plan_dosage_remove_intake_time),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (intake.dosesError is DosesError.Empty) {
                Text(
                    text = stringResource(R.string.screen_create_medication_plan_error_doses_empty),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            val selectedMedicationIds = intake.doses.mapNotNull { it.medication?.id }.toSet()
            val allMedications = medications.itemSnapshotList.items

            intake.doses.forEach { dose ->
                val availableMedications = allMedications.filter {
                    it.id !in selectedMedicationIds || it.id == dose.medication?.id
                }
                DoseItem(
                    availableMedications = availableMedications,
                    dose = dose,
                    onRemoveClick = { onRemoveDoseClick(dose) },
                    onAmountChange = { amount -> onDoseAmountChange(dose, amount) },
                    onMedicationSelected = { medication ->
                        onDoseMedicationSelected(
                            dose,
                            medication
                        )
                    }
                )
            }

            val label = stringResource(R.string.screen_create_medication_plan_dosage_add_medication)
            TextButton(
                onClick = onAddDoseClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = AppIcons.Add, contentDescription = label)
                Text(text = label)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DoseItem(
    availableMedications: List<Medication>,
    dose: DoseInput,
    onRemoveClick: () -> Unit,
    onAmountChange: (String) -> Unit,
    onMedicationSelected: (Medication) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = dose.medication?.name ?: "",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                    readOnly = true,
                    label = { Text(text = stringResource(R.string.screen_create_medication_plan_dosage_select_medication)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    isError = dose.doseError != null,
                    singleLine = true
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    availableMedications.forEach { medication ->
                        DropdownMenuItem(
                            text = { Text(medication.name) },
                            onClick = {
                                onMedicationSelected(medication)
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = dose.dose,
                onValueChange = onAmountChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.screen_create_medication_plan_dosage_dose)) },
                suffix = { Text(text = dose.medication?.doseUnit ?: "") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                isError = dose.doseError != null,
                supportingText = {
                    if (dose.doseError != null) {
                        val message = when (dose.doseError) {
                            DoseError.Empty -> stringResource(R.string.screen_create_medication_plan_error_dose_empty)
                            DoseError.Invalid -> stringResource(R.string.screen_create_medication_plan_error_dose_invalid)
                        }
                        Text(text = message)
                    }
                },
                singleLine = true
            )
        }

        IconButton(onClick = onRemoveClick) {
            Icon(
                imageVector = AppIcons.RemoveCircle,
                contentDescription = stringResource(R.string.screen_create_medication_plan_dosage_remove_medication)
            )
        }
    }
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
            onAddDoseClick = {},
            onRemoveDoseClick = { _, _ -> },
            onDoseAmountChange = { _, _, _ -> },
            onDoseMedicationSelected = { _, _, _ -> }
        )
    }
}