package io.floriax.medschedule.feature.medication.ui

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.feature.medication.R
import io.floriax.medschedule.shared.designsystem.component.MedScheduleTopAppBar
import io.floriax.medschedule.shared.designsystem.icon.AppIcons
import io.floriax.medschedule.shared.designsystem.theme.AppTheme
import io.floriax.medschedule.shared.ui.MedScheduleLoadingIndicator
import io.floriax.medschedule.shared.ui.extension.collectSideEffect
import io.floriax.medschedule.shared.ui.extension.collectState
import kotlinx.coroutines.flow.flowOf
import io.floriax.medschedule.shared.ui.R as sharedUiR

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/26
 */
@Composable
fun MedicineCabinetRoute(
    onAddMedicationClick: () -> Unit,
    onEditMedicationClick: (Medication) -> Unit,
    viewModel: MedicineCabinetViewModel = hiltViewModel(),
) {

    val state by viewModel.collectState()

    val medicationPagingItems = viewModel.pagedMedications.collectAsLazyPagingItems()

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            DeleteMedicationSuccess -> {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.screen_medicine_cabinet_delete_medication_success)
                )
            }

            DeleteMedicationFailure -> {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.screen_medicine_cabinet_delete_medication_failure)
                )
            }
        }
    }

    state.medicationToDelete?.let { medication ->
        DeleteMedicationConfirmationDialog(
            medicationName = medication.name,
            onDismissRequest = { viewModel.updateMedicationToDelete(null) },
            onConfirmClick = {
                viewModel.attemptDeleteMedication(medication)
            }
        )
    }

    MedicineCabinetScreen(
        medicationPagingItems = medicationPagingItems,
        snackbarHostState = snackbarHostState,
        onAddMedicationClick = onAddMedicationClick,
        onEditMedicationClick = onEditMedicationClick,
        onDeleteMedicationClick = viewModel::updateMedicationToDelete
    )
}

@Composable
private fun MedicineCabinetScreen(
    medicationPagingItems: LazyPagingItems<Medication>,
    snackbarHostState: SnackbarHostState,
    onAddMedicationClick: () -> Unit,
    onEditMedicationClick: (Medication) -> Unit,
    onDeleteMedicationClick: (Medication) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MedicineCabinetTopBar()
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddMedicationClick) {
                Icon(
                    imageVector = AppIcons.Add,
                    contentDescription = stringResource(R.string.screen_medicine_cabinet_add_medication)
                )
            }
        }
    ) { paddingValues ->
        MedicineCabinetContent(
            medicationPagingItems = medicationPagingItems,
            onEditMedicationClick = onEditMedicationClick,
            onDeleteMedicationClick = onDeleteMedicationClick,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MedicineCabinetTopBar(
    modifier: Modifier = Modifier
) {
    MedScheduleTopAppBar(
        titleRes = R.string.screen_medicine_cabinet_title,
        subtitleRes = R.string.screen_medicine_cabinet_subtitle,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun MedicineCabinetContent(
    medicationPagingItems: LazyPagingItems<Medication>,
    onEditMedicationClick: (Medication) -> Unit,
    onDeleteMedicationClick: (Medication) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        medicationPagingItems.loadState.refresh == LoadState.Loading ->
            MedScheduleLoadingIndicator(modifier = modifier.fillMaxSize())

        medicationPagingItems.itemCount == 0 ->
            EmptyMedicationList(modifier = modifier.padding(top = 128.dp))

        else -> {
            MedicationList(
                medicationPagingItems = medicationPagingItems,
                onEditMedicationClick = onEditMedicationClick,
                onDeleteMedicationClick = onDeleteMedicationClick,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun EmptyMedicationList(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = AppIcons.MedicationBorder,
            contentDescription = null,
            modifier = Modifier.size(96.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.screen_medicine_cabinet_empty_medication),
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.screen_medicine_cabinet_click_to_add_medication),
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun MedicationList(
    medicationPagingItems: LazyPagingItems<Medication>,
    onEditMedicationClick: (Medication) -> Unit,
    onDeleteMedicationClick: (Medication) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    ) {
        items(
            count = medicationPagingItems.itemCount,
            key = medicationPagingItems.itemKey { item -> item.id },
        ) { index ->
            val item = medicationPagingItems[index]
            if (item != null) {
                MedicationCard(
                    medication = item,
                    onEditClick = { onEditMedicationClick(item) },
                    onDeleteClick = { onDeleteMedicationClick(item) }
                )
            }
        }
    }
}

@Composable
private fun MedicationCard(
    medication: Medication,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (showMenu, setShowMenu) = remember { mutableStateOf(false) }

    ElevatedCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = medication.name,
                    modifier = Modifier.basicMarquee(),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = medication.notes.ifBlank {
                        stringResource(R.string.screen_medicine_cabinet_medication_card_no_notes)
                    },
                    color = MaterialTheme.colorScheme.outline,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            medication.stock?.let { stock ->
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = stringResource(R.string.screen_medicine_cabinet_stock_prefix),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = medication.stock.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = medication.doseUnit,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            Box {
                IconButton(onClick = { setShowMenu(!showMenu) }) {
                    Icon(
                        imageVector = AppIcons.MoreVert,
                        contentDescription = stringResource(sharedUiR.string.shared_ui_more),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                MedicationMenu(
                    expanded = showMenu,
                    onDismissRequest = { setShowMenu(false) },
                    onEditClick = {
                        setShowMenu(false)
                        onEditClick()
                    },
                    onDeleteClick = {
                        setShowMenu(false)
                        onDeleteClick()
                    }
                )
            }
        }
    }
}

@Composable
private fun MedicationMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(sharedUiR.string.shared_ui_edit)) },
            onClick = onEditClick,
            leadingIcon = {
                Icon(
                    imageVector = AppIcons.Edit,
                    contentDescription = stringResource(sharedUiR.string.shared_ui_edit)
                )
            }
        )
        DropdownMenuItem(
            text = {
                Text(
                    stringResource(sharedUiR.string.shared_ui_delete),
                    color = MaterialTheme.colorScheme.error
                )
            },
            onClick = onDeleteClick,
            leadingIcon = {
                Icon(
                    imageVector = AppIcons.Delete,
                    contentDescription = stringResource(sharedUiR.string.shared_ui_delete),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeleteMedicationConfirmationDialog(
    medicationName: String,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirmClick) {
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
        title = { Text(text = stringResource(R.string.dialog_delete_medication_title)) },
        text = {
            Text(
                text = stringResource(
                    R.string.dialog_delete_medication_content,
                    medicationName
                )
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun EmptyMedicationListPreview() {
    AppTheme {
        EmptyMedicationList()
    }
}

@Preview(showBackground = true)
@Composable
private fun MedicationCardPreview() {
    AppTheme {
        MedicationCard(
            medication = Medication(
                name = "Aspirin",
                stock = "10".toBigDecimal(),
                doseUnit = "mg",
                notes = "Take with food"
            ),
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MedicineCabinetScreenPreview() {
    AppTheme {
        val pagingData = PagingData.from(emptyList<Medication>())
        val medicationPagingItems = flowOf(pagingData).collectAsLazyPagingItems()
        MedicineCabinetScreen(
            medicationPagingItems = medicationPagingItems,
            snackbarHostState = remember { SnackbarHostState() },
            onAddMedicationClick = {},
            onEditMedicationClick = {},
            onDeleteMedicationClick = {}
        )
    }
}