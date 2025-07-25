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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.floriax.medschedule.core.common.extension.ifNullOrBlank
import io.floriax.medschedule.core.domain.enums.MedicationLogType
import io.floriax.medschedule.core.domain.enums.MedicationState
import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.core.domain.model.MedicationLog
import io.floriax.medschedule.core.domain.model.TakenMedication
import io.floriax.medschedule.feature.medicationlog.R
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
                    state.medicationLog
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
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            MedicationLogDetailHeader(
                medicationTime = medicationLog.medicationTime,
                medicationLogType = medicationLog.type,
            )
        }

        item {
            TakenMedicationList(
                takenMedications = medicationLog.takenMedications,
            )
        }

        item {
            NotesSection(notes = medicationLog.notes)
        }
    }
}

@Composable
private fun MedicationLogDetailHeader(
    medicationTime: Instant,
    medicationLogType: MedicationLogType,
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = medicationTime.formatFullLocalDateTime(),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            if (medicationLogType == MedicationLogType.MANUAL) {
                ManualTag()
            }
        }
    }
}

@Composable
private fun TakenMedicationList(
    takenMedications: List<TakenMedication>,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = stringResource(R.string.screen_medication_log_detail_medications_header),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            takenMedications.forEachIndexed { index, takenMedication ->
                TakenMedicationItem(takenMedication = takenMedication)
                Spacer(modifier = Modifier.height(8.dp))
                if (index < takenMedications.lastIndex) {
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun TakenMedicationItem(
    takenMedication: TakenMedication,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = takenMedication.medication.name,
            modifier = Modifier
                .weight(1f)
                .basicMarquee()
                .alignBy(LastBaseline),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "${takenMedication.dose.toPlainString()} ${takenMedication.medication.doseUnit}",
            modifier = Modifier.alignBy(LastBaseline),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun NotesSection(
    notes: String?,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.screen_medication_log_detail_notes_header),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = notes.ifNullOrBlank {
                    stringResource(R.string.screen_medication_log_detail_notes_blank)
                }
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