package io.floriax.medschedule.feature.medicationlog.ui.detail

import androidx.compose.foundation.basicMarquee
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import io.floriax.medschedule.core.common.extension.ifNullOrBlank
import io.floriax.medschedule.core.domain.enums.MedicationLogType
import io.floriax.medschedule.core.domain.enums.MedicationState
import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.core.domain.model.MedicationLog
import io.floriax.medschedule.core.domain.model.TakenMedication
import io.floriax.medschedule.feature.medicationlog.R
import io.floriax.medschedule.feature.medicationlog.ui.component.StatusIndicator
import io.floriax.medschedule.feature.medicationlog.ui.component.TakenMedicationItem
import io.floriax.medschedule.shared.designsystem.theme.AppTheme
import io.floriax.medschedule.shared.ui.component.BackButton
import io.floriax.medschedule.shared.ui.component.DeleteButton
import io.floriax.medschedule.shared.ui.component.EditButton
import io.floriax.medschedule.shared.ui.component.ErrorIndicator
import io.floriax.medschedule.shared.ui.component.LabeledCheckbox
import io.floriax.medschedule.shared.ui.component.LoadingIndicator
import io.floriax.medschedule.shared.ui.component.ManualTag
import io.floriax.medschedule.shared.ui.extension.collectSideEffect
import io.floriax.medschedule.shared.ui.extension.collectState
import io.floriax.medschedule.shared.ui.extension.formatFullLocalDateTime
import java.time.Instant
import java.time.ZoneId
import io.floriax.medschedule.shared.ui.R as sharedUiR

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/14
 */
@Composable
fun MedicationLogDetailRoute(
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    viewModel: MedicationLogDetailViewModel = hiltViewModel()
) {

    val state by viewModel.collectState()

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            DeleteMedicationLogSuccess -> onBackClick()

            DeleteMedicationLogFailure -> {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.screen_medication_log_detail_delete_medication_log_failure)
                )
            }
        }
    }

    if (state.showDeleteDialog) {
        state.medicationLog?.let { medicationLog ->
            DeleteMedicationLogConfirmationDialog(
                medicationLog = medicationLog,
                onDismissRequest = { viewModel.toggleDeleteDialog(false) },
                onConfirmClick = viewModel::attemptDeleteMedicationLog
            )
        }
    }

    MedicationLogDetailScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onBackClick = onBackClick,
        onEditClick = onEditClick,
        onDeleteClick = { viewModel.toggleDeleteDialog(true) }
    )
}

@Composable
private fun MedicationLogDetailScreen(
    state: MedicationLogDetailUiState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MedicationLogDetailTopBar(
                onBackClick = onBackClick,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when {
                state.loading -> LoadingIndicator(modifier = Modifier.fillMaxSize())
                state.error -> ErrorIndicator(modifier = Modifier.fillMaxSize())
                state.medicationLog != null -> MedicationLogDetailContent(
                    medicationLog = state.medicationLog
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MedicationLogDetailTopBar(
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.screen_medication_log_detail_title),
                modifier = Modifier.basicMarquee(),
                maxLines = 1,
            )
        },
        modifier = modifier,
        navigationIcon = {
            BackButton(onClick = onBackClick)
        },
        actions = {
            EditButton(onClick = onEditClick)
            DeleteButton(onClick = onDeleteClick)
        }
    )
}

@Composable
private fun MedicationLogDetailContent(
    medicationLog: MedicationLog,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            InfoCard(medicationLog)
        }

        if (!medicationLog.notes.isNullOrBlank()) {
            item {
                NotesCard(notes = medicationLog.notes)
            }
        }
    }
}

@Composable
private fun InfoCard(log: MedicationLog, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = log.medicationTime.formatFullLocalDateTime(),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMedium
                )
                if (log.type == MedicationLogType.MANUAL) {
                    ManualTag()
                }
            }

            StatusIndicator(log.state)

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            Text(
                text = stringResource(R.string.screen_medication_log_detail_medications_header),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                log.takenMedications.forEach { takenMedication ->
                    TakenMedicationItem(takenMedication = takenMedication)
                }
            }
        }
    }
}

@Composable
private fun NotesCard(
    notes: String?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.screen_medication_log_detail_notes_header),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = notes.ifNullOrBlank {
                    stringResource(R.string.screen_medication_log_detail_notes_blank)
                },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun DeleteMedicationLogConfirmationDialog(
    medicationLog: MedicationLog,
    onDismissRequest: () -> Unit,
    onConfirmClick: (Boolean) -> Unit
) {

    val hasMedicationDeductFromStock = medicationLog.takenMedications.any { takenMedication ->
        takenMedication.deductFromStock
    }

    var restoreMedicationStore by remember { mutableStateOf(hasMedicationDeductFromStock) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = { onConfirmClick(restoreMedicationStore) }) {
                Text(
                    text = stringResource(sharedUiR.string.shared_ui_confirm),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(sharedUiR.string.shared_ui_cancel))
            }
        },
        title = { Text(text = stringResource(R.string.dialog_delete_medication_log_confirmation_title)) },
        text = {
            Column {
                Text(text = stringResource(R.string.dialog_delete_medication_log_confirmation_content))
                if (hasMedicationDeductFromStock) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LabeledCheckbox(
                        label = {
                            Text(text = stringResource(R.string.dialog_delete_medication_log_confirmation_restore_medication_stock))
                        },
                        checked = restoreMedicationStore,
                        onCheckedChange = { checked -> restoreMedicationStore = checked }
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun MedicationLogDetailScreenPreview() {
    val takenMedications = listOf(
        TakenMedication(
            medication = Medication(
                id = 1,
                name = "Ibuprofen",
                stock = "10".toBigDecimal(),
                doseUnit = "tablets",
                notes = "Take with food",
            ),
            dose = "2".toBigDecimal(),
            deductFromStock = false
        ),
        TakenMedication(
            medication = Medication(
                id = 2,
                name = "Aspirin",
                stock = "10".toBigDecimal(),
                doseUnit = "tablets",
                notes = "Take with food",
            ),
            dose = "2".toBigDecimal(),
            deductFromStock = true
        )
    )

    val medicationLog = MedicationLog(
        id = 1,
        medicationTime = Instant.now(),
        takenMedications = takenMedications,
        state = MedicationState.TAKEN,
        type = MedicationLogType.MANUAL,
        timeZone = ZoneId.systemDefault(),
        notes = "Take with food"
    )

    AppTheme {
        MedicationLogDetailScreen(
            state = MedicationLogDetailUiState(
                loading = false,
                medicationLog = medicationLog
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onBackClick = {},
            onEditClick = {},
            onDeleteClick = {},
        )
    }
}