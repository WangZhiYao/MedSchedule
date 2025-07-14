package io.floriax.medschedule.feature.medication.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.floriax.medschedule.core.common.extension.isValidStock
import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.core.domain.model.MedicationRecord
import io.floriax.medschedule.core.domain.model.TakenMedication
import io.floriax.medschedule.feature.medication.R
import io.floriax.medschedule.shared.designsystem.icon.AppIcons
import io.floriax.medschedule.shared.designsystem.theme.AppTheme
import io.floriax.medschedule.shared.ui.BackButton
import io.floriax.medschedule.shared.ui.DeleteButton
import io.floriax.medschedule.shared.ui.EditButton
import io.floriax.medschedule.shared.ui.ErrorIndicator
import io.floriax.medschedule.shared.ui.LoadingIndicator
import io.floriax.medschedule.shared.ui.extension.collectSideEffect
import io.floriax.medschedule.shared.ui.extension.collectState
import io.floriax.medschedule.shared.ui.extension.formatLocalDateTime
import kotlinx.coroutines.flow.flowOf
import java.math.BigDecimal
import java.time.Instant
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
    onEditClick: (Medication) -> Unit,
    onMedicationRecordClick: (MedicationRecord) -> Unit,
    viewModel: MedicationDetailViewModel = hiltViewModel()
) {

    val state by viewModel.collectState()
    val medicationRecordPagingItems = viewModel.medicationRecords.collectAsLazyPagingItems()

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            DeleteMedicationSuccess -> {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.screen_medication_detail_delete_medication_success)
                )
            }

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
        medicationRecordPagingItems = medicationRecordPagingItems,
        onBackClick = onBackClick,
        onEditClick = onEditClick,
        onDeleteClick = { viewModel.toggleDeleteDialog(true) },
        onAddStockClick = { viewModel.toggleAddStockBottomSheet(true) },
        onMedicationRecordClick = onMedicationRecordClick
    )
}

@Composable
private fun MedicationDetailScreen(
    state: MedicationDetailViewState,
    snackbarHostState: SnackbarHostState,
    medicationRecordPagingItems: LazyPagingItems<MedicationRecord>,
    onBackClick: () -> Unit,
    onEditClick: (Medication) -> Unit,
    onDeleteClick: () -> Unit,
    onAddStockClick: () -> Unit,
    onMedicationRecordClick: (MedicationRecord) -> Unit,
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
                state.medication != null -> MedicationRecordList(
                    medication = state.medication,
                    medicationRecords = medicationRecordPagingItems,
                    onAddStockClick = onAddStockClick,
                    onMedicationRecordClick = onMedicationRecordClick,
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
    onEditClick: (Medication) -> Unit,
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
                EditButton(onClick = { onEditClick(medication) })
                DeleteButton(onClick = onDeleteClick)
            }
        }
    )
}

@Composable
private fun Header(
    medication: Medication,
    onAddStockClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        StockCard(
            stock = medication.stock,
            doseUnit = medication.doseUnit,
            onAddStockClick = onAddStockClick,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        NotesCard(
            notes = medication.notes,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.screen_medication_detail_medication_records),
            modifier = Modifier.padding(horizontal = 16.dp),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
        )

        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
private fun StockCard(
    stock: BigDecimal?,
    doseUnit: String,
    onAddStockClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.screen_medication_detail_stock),
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                val value = if (stock == null) {
                    stringResource(R.string.screen_medication_detail_stock_not_set)
                } else {
                    stock.toPlainString()
                }
                Row {
                    Text(
                        text = value,
                        modifier = Modifier.alignBy(LastBaseline),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineLarge
                    )
                    if (stock != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = doseUnit,
                            modifier = Modifier.alignBy(LastBaseline)
                        )
                    }
                }
            }

            val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(onSurfaceVariant.copy(alpha = 0.1f))
                    .border(1.dp, onSurfaceVariant, CircleShape)
                    .clickable(onClick = onAddStockClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = AppIcons.Add,
                    contentDescription = stringResource(R.string.screen_medication_detail_add_stock),
                    tint = onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun NotesCard(
    notes: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.screen_medication_detail_notes),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = notes.ifBlank { stringResource(R.string.screen_medication_detail_no_notes) },
                color = if (notes.isBlank()) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun MedicationRecordList(
    medication: Medication,
    medicationRecords: LazyPagingItems<MedicationRecord>,
    onAddStockClick: () -> Unit,
    onMedicationRecordClick: (MedicationRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 8.dp),
    ) {
        item {
            Header(
                medication = medication,
                onAddStockClick = onAddStockClick,
            )
        }

        when (medicationRecords.loadState.refresh) {
            LoadState.Loading -> item {
                LoadingIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }

            is LoadState.Error -> item {
                ErrorIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }

            else -> {
                if (medicationRecords.itemCount == 0) {
                    item {
                        Text(
                            text = stringResource(R.string.screen_medication_detail_no_medication_records),
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    items(medicationRecords.itemCount) { index ->
                        val medicationRecord = medicationRecords[index]
                        if (medicationRecord != null) {
                            val takenMedication =
                                medicationRecord.takenMedications.firstOrNull { takenMedication ->
                                    takenMedication.medication.id == medication.id
                                }
                            if (takenMedication != null) {
                                MedicationRecordItem(
                                    firstItem = index == 0,
                                    lastItem = index == medicationRecords.itemCount - 1,
                                    medicationTime = medicationRecord.medicationTime,
                                    takenMedication = takenMedication,
                                    notes = medicationRecord.notes,
                                    onMedicationRecordClick = {
                                        onMedicationRecordClick(medicationRecord)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MedicationRecordItem(
    firstItem: Boolean,
    lastItem: Boolean,
    medicationTime: Instant,
    takenMedication: TakenMedication,
    notes: String,
    onMedicationRecordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onMedicationRecordClick() },
    ) {
        val (
            topDividerRef,
            iconRef,
            bottomDividerRef,
            timeTextRef,
            doseTextRef,
            notesSurfaceRef
        ) = createRefs()

        TimelineDivider(
            visible = !firstItem,
            modifier = Modifier.constrainAs(topDividerRef) {
                height = Dimension.value(16.dp)
                top.linkTo(iconRef.bottom)
                bottom.linkTo(iconRef.top)
                centerHorizontallyTo(iconRef)
            }
        )

        Icon(
            imageVector = AppIcons.CircleBorder,
            contentDescription = null,
            modifier = Modifier.constrainAs(iconRef) {
                start.linkTo(parent.start, margin = 16.dp)
                top.linkTo(topDividerRef.bottom)
            },
            tint = MaterialTheme.colorScheme.primary
        )

        TimelineDivider(
            visible = !lastItem,
            modifier = Modifier.constrainAs(bottomDividerRef) {
                height = if (notes.isNotBlank()) {
                    Dimension.fillToConstraints
                } else {
                    Dimension.value(16.dp)
                }
                top.linkTo(iconRef.bottom)
                bottom.linkTo(parent.bottom)
                centerHorizontallyTo(iconRef)
            }
        )

        Text(
            text = medicationTime.formatLocalDateTime(),
            modifier = Modifier
                .constrainAs(timeTextRef) {
                    start.linkTo(iconRef.end, margin = 8.dp)
                    end.linkTo(doseTextRef.start, margin = 8.dp)
                    centerVerticallyTo(iconRef)
                    width = Dimension.fillToConstraints
                }
                .basicMarquee(),
        )

        Text(
            text = "${takenMedication.dose.toPlainString()} ${takenMedication.medication.doseUnit}",
            modifier = Modifier.constrainAs(doseTextRef) {
                baseline.linkTo(timeTextRef.baseline)
                end.linkTo(parent.end, margin = 16.dp)
            },
            fontWeight = FontWeight.Bold
        )

        if (notes.isNotBlank()) {
            Surface(
                modifier = Modifier
                    .constrainAs(notesSurfaceRef) {
                        start.linkTo(timeTextRef.start)
                        top.linkTo(timeTextRef.bottom, margin = 4.dp)
                        end.linkTo(doseTextRef.end)
                        bottom.linkTo(parent.bottom, margin = 8.dp)
                        width = Dimension.fillToConstraints
                    },
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = MaterialTheme.shapes.extraSmall
            ) {
                Box(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                    Text(text = notes, overflow = TextOverflow.Ellipsis, maxLines = 2)
                }
            }
        }
    }
}

@Composable
private fun TimelineDivider(
    visible: Boolean,
    modifier: Modifier = Modifier,
) {
    if (visible) {
        VerticalDivider(
            modifier = modifier,
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.outline
        )
    } else {
        Spacer(modifier = modifier)
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
    val pagingData = PagingData.from(emptyList<MedicationRecord>())
    val medicationRecords = flowOf(pagingData).collectAsLazyPagingItems()
    AppTheme {
        MedicationDetailScreen(
            state = MedicationDetailViewState(
                loading = false,
                error = false,
                medication = Medication(
                    name = "Ibuprofen",
                    stock = "10".toBigDecimal(),
                    doseUnit = "tablets",
                    notes = "Take with food",
                )
            ),
            snackbarHostState = remember { SnackbarHostState() },
            medicationRecordPagingItems = medicationRecords,
            onBackClick = {},
            onEditClick = {},
            onDeleteClick = {},
            onAddStockClick = {},
            onMedicationRecordClick = {}
        )
    }
}