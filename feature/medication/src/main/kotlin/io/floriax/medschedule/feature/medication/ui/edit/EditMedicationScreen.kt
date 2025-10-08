package io.floriax.medschedule.feature.medication.ui.edit

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import io.floriax.medschedule.feature.medication.R
import io.floriax.medschedule.feature.medication.ui.form.MedicationForm
import io.floriax.medschedule.shared.designsystem.theme.AppTheme
import io.floriax.medschedule.shared.ui.component.BackButton
import io.floriax.medschedule.shared.ui.extension.collectSideEffect
import io.floriax.medschedule.shared.ui.extension.collectState

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
        MedicationForm(
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

@Preview(showBackground = true)
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