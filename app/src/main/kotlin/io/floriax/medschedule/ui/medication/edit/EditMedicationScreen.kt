package io.floriax.medschedule.ui.medication.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.floriax.medschedule.R
import io.floriax.medschedule.common.ext.collectSideEffect
import io.floriax.medschedule.common.ext.collectState
import io.floriax.medschedule.ui.designsystem.AppIcons
import io.floriax.medschedule.ui.theme.AppTheme

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/31
 */
@Composable
fun EditMedicationRoute(
    onBackClick: () -> Unit,
    onMedicationUpdated: () -> Unit,
    viewModel: EditMedicationViewModel = hiltViewModel(),
) {

    val state by viewModel.collectState()

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            EditMedicationSuccess -> {
                onMedicationUpdated()
            }

            EditMedicationFailed -> {
                snackbarHostState.showSnackbar(context.getString(R.string.error_medication_update_failed))
            }
        }
    }

    EditMedicationScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onBackClick = onBackClick,
        onNameChange = viewModel::onNameChange,
        onDoseUnitChange = viewModel::onDoseUnitChange,
        onRemarkChange = viewModel::onRemarkChange,
        onSaveMedicationClick = viewModel::attemptUpdateMedication
    )
}

@Composable
private fun EditMedicationScreen(
    state: EditMedicationViewState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onNameChange: (String) -> Unit,
    onDoseUnitChange: (String) -> Unit,
    onRemarkChange: (String) -> Unit,
    onSaveMedicationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            EditMedicationTopBar(
                onBackClick = onBackClick,
                onSaveClick = onSaveMedicationClick
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = onNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.edit_medication_name)) },
                supportingText = {
                    Text(text = if (state.nameError) stringResource(R.string.error_medication_name_empty) else "")
                },
                isError = state.nameError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                singleLine = true
            )

            OutlinedTextField(
                value = state.doseUnit,
                onValueChange = onDoseUnitChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.edit_medication_dose_unit)) },
                supportingText = { Text(text = "") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                singleLine = true
            )

            OutlinedTextField(
                value = state.remark,
                onValueChange = onRemarkChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.edit_medication_remark)) },
                maxLines = 3,
                minLines = 3
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditMedicationTopBar(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.edit_medication_title))
        },
        modifier = modifier.fillMaxWidth(),
        navigationIcon = {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = AppIcons.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        actions = {
            IconButton(
                onClick = onSaveClick
            ) {
                Icon(
                    imageVector = AppIcons.Save,
                    contentDescription = stringResource(R.string.save)
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun AddMedicationScreenPreview() {
    AppTheme {
        EditMedicationScreen(
            state = EditMedicationViewState(),
            snackbarHostState = remember { SnackbarHostState() },
            onBackClick = {},
            onNameChange = {},
            onDoseUnitChange = {},
            onRemarkChange = {},
            onSaveMedicationClick = {},
        )
    }
}
