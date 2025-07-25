package io.floriax.medschedule.feature.medication.ui.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.floriax.medschedule.feature.medication.R
import io.floriax.medschedule.shared.designsystem.theme.AppTheme
import io.floriax.medschedule.shared.ui.component.BackButton
import io.floriax.medschedule.shared.ui.extension.collectSideEffect
import io.floriax.medschedule.shared.ui.extension.collectState
import io.floriax.medschedule.shared.ui.R as sharedUiR

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/28
 */
@Composable
fun EditMedicationRoute(
    onBackClick: () -> Unit,
    viewModel: EditMedicationViewModel = hiltViewModel()
) {

    val state by viewModel.collectState()

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val nameFocusRequester = remember { FocusRequester() }
    val stockFocusRequester = remember { FocusRequester() }
    val doseUnitFocusRequester = remember { FocusRequester() }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            MedicationNotFound -> {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.screen_edit_medication_error_medication_not_found)
                )
            }

            GetMedicationFailure -> {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.screen_edit_medication_error_get_medication_failed)
                )
            }

            is UpdateMedicationSuccess -> {
                onBackClick()
            }

            UpdateMedicationFailure -> {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.screen_edit_medication_error_update_failed)
                )
            }

            RequestFocusOnNameField -> {
                nameFocusRequester.requestFocus()
            }

            RequestFocusOnStockField -> {
                stockFocusRequester.requestFocus()
            }

            RequestFocusOnDoseUnitField -> {
                doseUnitFocusRequester.requestFocus()
            }

        }
    }

    EditMedicationScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        nameFocusRequester = nameFocusRequester,
        stockFocusRequester = stockFocusRequester,
        doseUnitFocusRequester = doseUnitFocusRequester,
        onBackClick = onBackClick,
        onMedicationNameChange = viewModel::onMedicationNameChange,
        onStockStringChange = viewModel::onStockStringChange,
        onDoseUnitChange = viewModel::onDoseUnitChange,
        onNotesChange = viewModel::onNotesChange,
        onSaveClick = viewModel::onSaveClick
    )
}

@Composable
private fun EditMedicationScreen(
    state: EditMedicationUiState,
    snackbarHostState: SnackbarHostState,
    nameFocusRequester: FocusRequester,
    stockFocusRequester: FocusRequester,
    doseUnitFocusRequester: FocusRequester,
    onBackClick: () -> Unit,
    onMedicationNameChange: (String) -> Unit,
    onStockStringChange: (String) -> Unit,
    onDoseUnitChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            EditMedicationTopBar(onBackClick = onBackClick)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        EditMedicationContent(
            state = state,
            nameFocusRequester = nameFocusRequester,
            stockFocusRequester = stockFocusRequester,
            doseUnitFocusRequester = doseUnitFocusRequester,
            onMedicationNameChange = onMedicationNameChange,
            onStockStringChange = onStockStringChange,
            onDoseUnitChange = onDoseUnitChange,
            onNotesChange = onNotesChange,
            onSaveClick = onSaveClick,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditMedicationTopBar(
    onBackClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.screen_edit_medication_title))
        },
        navigationIcon = {
            BackButton(onClick = onBackClick)
        }
    )
}

@Composable
private fun EditMedicationContent(
    state: EditMedicationUiState,
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
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            value = state.medicationName,
            onValueChange = onMedicationNameChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(nameFocusRequester),
            label = { Text(text = stringResource(R.string.screen_edit_medication_medication_name_label)) },
            placeholder = { Text(text = stringResource(R.string.screen_edit_medication_medication_name_placeholder)) },
            supportingText = {
                Text(text = if (state.medicationNameError) stringResource(R.string.screen_edit_medication_error_name_empty) else "")
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
            label = { Text(text = stringResource(R.string.screen_edit_medication_stock_label)) },
            placeholder = { Text(text = stringResource(R.string.screen_edit_medication_stock_placeholder)) },
            supportingText = {
                Text(text = if (state.stockError) stringResource(R.string.screen_edit_medication_error_stock_invalid) else "")
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
            label = { Text(text = stringResource(R.string.screen_edit_medication_dose_unit_label)) },
            placeholder = { Text(text = stringResource(R.string.screen_edit_medication_dose_unit_placeholder)) },
            supportingText = {
                Text(text = if (state.doseUnitError) stringResource(R.string.screen_edit_medication_error_dose_unit_empty) else "")
            },
            isError = state.doseUnitError,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true
        )

        OutlinedTextField(
            value = state.notes,
            onValueChange = onNotesChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(R.string.screen_edit_medication_notes_label)) },
            placeholder = { Text(text = stringResource(R.string.screen_edit_medication_notes_placeholder)) },
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

@Preview
@Composable
private fun EditMedicationScreenPreview() {
    AppTheme {
        EditMedicationScreen(
            state = EditMedicationUiState(),
            snackbarHostState = remember { SnackbarHostState() },
            nameFocusRequester = remember { FocusRequester() },
            stockFocusRequester = remember { FocusRequester() },
            doseUnitFocusRequester = remember { FocusRequester() },
            onBackClick = {},
            onMedicationNameChange = {},
            onStockStringChange = {},
            onDoseUnitChange = {},
            onNotesChange = {},
            onSaveClick = {}
        )
    }
}