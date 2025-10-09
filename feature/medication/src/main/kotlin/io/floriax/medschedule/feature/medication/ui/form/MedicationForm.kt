package io.floriax.medschedule.feature.medication.ui.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import io.floriax.medschedule.feature.medication.R
import io.floriax.medschedule.shared.ui.R as sharedUiR

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/10/8
 */
@Composable
fun MedicationForm(
    state: MedicationFormUiState,
    nameFocusRequester: FocusRequester,
    stockFocusRequester: FocusRequester,
    doseUnitFocusRequester: FocusRequester,
    onMedicationNameChange: (String) -> Unit,
    onStockStringChange: (String) -> Unit,
    onDoseUnitChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        OutlinedTextField(
            value = state.medicationName,
            onValueChange = onMedicationNameChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(nameFocusRequester),
            label = { Text(text = stringResource(R.string.screen_add_medication_medication_name_label)) },
            placeholder = { Text(text = stringResource(R.string.screen_add_medication_medication_name_placeholder)) },
            supportingText = {
                if (state.medicationNameError) {
                    Text(text = stringResource(R.string.screen_add_medication_error_name_empty))
                }
            },
            isError = state.medicationNameError,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true
        )

        OutlinedTextField(
            value = state.stockString,
            onValueChange = onStockStringChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(stockFocusRequester),
            label = { Text(text = stringResource(R.string.screen_add_medication_stock_label)) },
            placeholder = { Text(text = stringResource(R.string.screen_add_medication_stock_placeholder)) },
            supportingText = {
                if (state.stockError) {
                    Text(text = stringResource(R.string.screen_add_medication_error_stock_invalid))
                }
            },
            isError = state.stockError,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true
        )

        OutlinedTextField(
            value = state.doseUnit,
            onValueChange = onDoseUnitChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(doseUnitFocusRequester),
            label = { Text(text = stringResource(R.string.screen_add_medication_dose_unit_label)) },
            placeholder = { Text(text = stringResource(R.string.screen_add_medication_dose_unit_placeholder)) },
            supportingText = {
                if (state.doseUnitError) {
                    Text(text = stringResource(R.string.screen_add_medication_error_dose_unit_empty))
                }
            },
            isError = state.doseUnitError,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true
        )

        OutlinedTextField(
            value = state.notes,
            onValueChange = onNotesChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(R.string.screen_add_medication_notes_label)) },
            placeholder = { Text(text = stringResource(R.string.screen_add_medication_notes_placeholder)) },
            maxLines = 3,
            minLines = 3
        )

        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp)
        ) {
            Text(text = stringResource(sharedUiR.string.shared_ui_save))
        }
    }
}

