package io.floriax.medschedule.feature.medication.ui.detail

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import io.floriax.medschedule.core.common.extension.ifNullOrBlank
import io.floriax.medschedule.core.common.extension.isValidStock
import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.core.domain.model.MedicationLog
import io.floriax.medschedule.feature.medication.R
import io.floriax.medschedule.shared.designsystem.icon.AppIcons
import io.floriax.medschedule.shared.designsystem.theme.AppTheme
import io.floriax.medschedule.shared.ui.component.BackButton
import io.floriax.medschedule.shared.ui.component.DeleteButton
import io.floriax.medschedule.shared.ui.component.EditButton
import io.floriax.medschedule.shared.ui.component.ErrorIndicator
import io.floriax.medschedule.shared.ui.component.LoadingIndicator
import io.floriax.medschedule.shared.ui.extension.collectSideEffect
import io.floriax.medschedule.shared.ui.extension.collectState
import io.floriax.medschedule.shared.ui.extension.formatLocalDateTime
import kotlinx.coroutines.flow.flowOf
import java.math.BigDecimal
import io.floriax.medschedule.shared.ui.R as sharedUiR

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/10
 */
@Composable
fun MedicationDetailRoute(
    onBackClick: () -> Unit,
    onEditClick: (Long) -> Unit,
    onMedicationLogClick: (Long) -> Unit,
    viewModel: MedicationDetailViewModel = hiltViewModel()
) {

    val state by viewModel.collectState()
    val medicationLogPagingItems = viewModel.medicationLogs.collectAsLazyPagingItems()

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            DeleteMedicationSuccess -> onBackClick()

            DeleteMedicationFailure -> {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.screen_medication_detail_delete_medication_failure)
                )
            }

            AddStockSuccess -> {
                viewModel.toggleAddStockBottomSheet(false)
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.screen_medication_detail_add_stock_success)
                )
            }

            AddStockFailure -> {
                viewModel.toggleAddStockBottomSheet(false)
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.screen_medication_detail_add_stock_failure)
                )
            }
        }
    }

    if (state.showDeleteDialog) {
        DeleteMedicationConfirmationDialog(
            medicationName = state.medication?.name.orEmpty(),
            onDismissRequest = { viewModel.toggleDeleteDialog(false) },
            onConfirmClick = viewModel::attemptDeleteMedication
        )
    }

    if (state.showAddStockBottomSheet) {
        AddStockBottomSheet(
            onDismissRequest = { viewModel.toggleAddStockBottomSheet(false) },
            onQuantityConfirm = viewModel::attemptAddStock
        )
    }

    MedicationDetailScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        medicationLogPagingItems = medicationLogPagingItems,
        onBackClick = onBackClick,
        onEditClick = onEditClick,
        onDeleteClick = { viewModel.toggleDeleteDialog(true) },
        onAddStockClick = { viewModel.toggleAddStockBottomSheet(true) },
        onMedicationLogClick = onMedicationLogClick
    )
}

@Composable
private fun MedicationDetailScreen(
    state: MedicationDetailUiState,
    snackbarHostState: SnackbarHostState,
    medicationLogPagingItems: LazyPagingItems<MedicationLog>,
    onBackClick: () -> Unit,
    onEditClick: (Long) -> Unit,
    onDeleteClick: () -> Unit,
    onAddStockClick: () -> Unit,
    onMedicationLogClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MedicationDetailTopBar(
                medication = state.medication,
                onBackClick = onBackClick,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            if (state.medication != null) {
                FloatingActionButton(onClick = onAddStockClick) {
                    Icon(
                        imageVector = AppIcons.Add,
                        contentDescription = stringResource(R.string.screen_medication_detail_add_stock)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when {
                state.loading -> LoadingIndicator(modifier = Modifier.fillMaxSize())
                state.error -> ErrorIndicator(modifier = Modifier.fillMaxSize())
                state.medication != null -> MedicationDetailContent(
                    medication = state.medication,
                    medicationLogs = medicationLogPagingItems,
                    onMedicationLogClick = onMedicationLogClick,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MedicationDetailTopBar(
    medication: Medication?,
    onBackClick: () -> Unit,
    onEditClick: (Long) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            val title = medication?.name ?: stringResource(sharedUiR.string.shared_ui_back)
            Text(text = title, modifier = Modifier.basicMarquee())
        },
        modifier = modifier,
        navigationIcon = {
            BackButton(onClick = onBackClick)
        },
        actions = {
            if (medication != null) {
                EditButton(onClick = { onEditClick(medication.id) })
                DeleteButton(onClick = onDeleteClick)
            }
        }
    )
}

@Composable
private fun MedicationDetailContent(
    medication: Medication,
    medicationLogs: LazyPagingItems<MedicationLog>,
    onMedicationLogClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            StockCard(medication = medication)
        }

        item {
            NotesCard(notes = medication.notes)
        }

        item {
            Text(
                text = stringResource(R.string.screen_medication_detail_medication_logs),
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }

        when (medicationLogs.loadState.refresh) {
            LoadState.Loading -> item {
                LoadingIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }

            is LoadState.Error -> item {
                ErrorIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }

            else -> {
                if (medicationLogs.itemCount == 0) {
                    item {
                        Text(
                            text = stringResource(R.string.screen_medication_detail_no_medication_logs),
                            modifier = Modifier.padding(top = 8.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                } else {
                    items(
                        count = medicationLogs.itemCount,
                        key = medicationLogs.itemKey { item -> item.id }
                    ) { index ->
                        val medicationLog = medicationLogs[index]
                        if (medicationLog != null) {
                            MedicationLogCard(
                                medicationLog = medicationLog,
                                medication = medication,
                                onCardClick = { onMedicationLogClick(medicationLog.id) },
                                modifier = if (index == 0) {
                                    Modifier.padding(top = 8.dp)
                                } else {
                                    Modifier
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StockCard(
    medication: Medication,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.screen_medication_detail_stock),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                val stock = medication.stock
                val stockText = stock?.toPlainString()
                    ?: stringResource(R.string.screen_medication_detail_stock_not_set)
                Text(
                    text = stockText,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                if (stock != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = medication.doseUnit,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
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
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.screen_medication_detail_notes),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = notes.ifNullOrBlank { stringResource(R.string.screen_medication_detail_no_notes) },
                color = if (notes.isNullOrBlank()) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun MedicationLogCard(
    medicationLog: MedicationLog,
    medication: Medication,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onCardClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = medicationLog.medicationTime.formatLocalDateTime(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                medicationLog.takenMedications.firstOrNull { takenMedication ->
                    takenMedication.medication.id == medication.id
                }?.let { takenMedication ->
                    Text(
                        text = "${takenMedication.dose.toPlainString()}${medication.doseUnit}",
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
            val notes = medicationLog.notes
            if (!notes.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = notes,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
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
                    R.string.dialog_delete_medication_content_format,
                    medicationName
                )
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddStockBottomSheet(
    onDismissRequest: () -> Unit,
    onQuantityConfirm: (BigDecimal) -> Unit,
    modifier: Modifier = Modifier
) {

    var stockString by remember { mutableStateOf("") }
    val stockError by remember {
        derivedStateOf {
            stockString.isNotBlank() && !stockString.isValidStock()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Text(
                text = stringResource(R.string.screen_medication_detail_add_stock),
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = stockString,
                onValueChange = { value -> stockString = value },
                label = { Text(text = stringResource(R.string.screen_medication_detail_quantity_to_add)) },
                placeholder = {
                    Text(text = stringResource(R.string.screen_medication_detail_quantity_to_add_placeholder))
                },
                supportingText = {
                    Text(text = if (stockError) stringResource(R.string.screen_medication_detail_error_stock_invalid) else "")
                },
                isError = stockError,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            TextButton(
                onClick = {
                    val stock = stockString.toBigDecimalOrNull()
                    if (stock != null) {
                        onQuantityConfirm(stock)
                    }
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 16.dp, bottom = 16.dp),
                enabled = !stockError
            ) {
                Text(text = stringResource(sharedUiR.string.shared_ui_confirm))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MedicationDetailScreenPreview() {
    val pagingData = PagingData.from(emptyList<MedicationLog>())
    val medicationLogs = flowOf(pagingData).collectAsLazyPagingItems()
    AppTheme {
        MedicationDetailScreen(
            state = MedicationDetailUiState(
                loading = false,
                error = false,
                medication = Medication(
                    id = 0,
                    name = "Ibuprofen",
                    stock = BigDecimal("10"),
                    doseUnit = "tablets",
                    notes = "Take with food",
                )
            ),
            snackbarHostState = remember { SnackbarHostState() },
            medicationLogPagingItems = medicationLogs,
            onBackClick = {},
            onEditClick = {},
            onDeleteClick = {},
            onAddStockClick = {},
            onMedicationLogClick = {}
        )
    }
}
