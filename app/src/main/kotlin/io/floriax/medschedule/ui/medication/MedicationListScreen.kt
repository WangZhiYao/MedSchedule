package io.floriax.medschedule.ui.medication

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import io.floriax.medschedule.R
import io.floriax.medschedule.common.ext.collectSideEffect
import io.floriax.medschedule.common.ext.collectState
import io.floriax.medschedule.domain.model.Medication
import io.floriax.medschedule.ui.designsystem.AppIcons

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
@Composable
fun MedicationListScreen(
    onBackClick: () -> Unit,
    onAddMedicationClick: () -> Unit,
    onEditMedicationClick: (Medication) -> Unit,
    viewModel: MedicationListViewModel = hiltViewModel()
) {

    val state by viewModel.collectState()

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    var medicationToDelete by remember { mutableStateOf<Medication?>(null) }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            DeleteMedicationSuccess -> {
                medicationToDelete = null
                snackbarHostState.showSnackbar(context.getString(R.string.medication_deleted))
            }

            DeleteMedicationFailure -> {
                medicationToDelete = null
                snackbarHostState.showSnackbar(context.getString(R.string.error_medication_delete_failed))
            }
        }
    }

    medicationToDelete?.let { medication ->
        DeleteMedicationConfirmationDialog(
            medicationName = medication.name,
            onDismissRequest = { medicationToDelete = null },
            onConfirmClick = {
                viewModel.attemptDeleteMedication(medication)
            }
        )
    }

    MedicationListScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onBackClick = onBackClick,
        onAddMedicationClick = onAddMedicationClick,
        onEditClick = onEditMedicationClick,
        onDeleteClick = { medication -> medicationToDelete = medication }
    )
}

@Composable
private fun MedicationListScreen(
    state: MedicationListViewState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onAddMedicationClick: () -> Unit,
    onEditClick: (Medication) -> Unit,
    onDeleteClick: (Medication) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val fabVisible by remember {
        derivedStateOf {
            when {
                listState.firstVisibleItemIndex > 0 -> false
                listState.firstVisibleItemScrollOffset > 0 -> false
                else -> true
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MedicationListTopBar(
                onBackClick = onBackClick
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            AnimatedVisibility(
                visible = fabVisible,
                enter = slideInVertically { height -> height } + scaleIn(),
                exit = scaleOut() + slideOutVertically { height -> height }
            ) {
                AddMedicationFab(onClick = onAddMedicationClick)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            state = listState
        ) {
            items(
                items = state.medicationList,
                key = { medication -> medication.id }
            ) { medication ->
                MedicationItem(
                    medication = medication,
                    onEditClick = { onEditClick(medication) },
                    onDeleteClick = { onDeleteClick(medication) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MedicationListTopBar(
    onBackClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.medication_list_title))
        },
        navigationIcon = {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = AppIcons.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        }
    )
}

@Composable
private fun AddMedicationFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = AppIcons.Add,
            contentDescription = stringResource(R.string.medication_list_add_medication)
        )
    }
}

@Composable
private fun MedicationItem(
    medication: Medication,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    var showMenu by remember { mutableStateOf(false) }

    Column {
        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(
                        R.string.medication_name_dose_unit,
                        medication.name,
                        medication.doseUnit
                    )
                )
            },
            modifier = modifier,
            supportingContent = {
                Text(
                    text = if (medication.remark.isNotBlank()) {
                        medication.remark
                    } else {
                        stringResource(R.string.no_remark)
                    },
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            },
            trailingContent = {
                Box {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(
                            imageVector = AppIcons.More,
                            contentDescription = stringResource(R.string.more_action),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    MedicationMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        onEditClick = onEditClick,
                        onDeleteClick = onDeleteClick
                    )
                }
            }
        )
        HorizontalDivider()
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
            text = { Text(stringResource(R.string.edit)) },
            onClick = onEditClick,
            leadingIcon = {
                Icon(
                    imageVector = AppIcons.Edit,
                    contentDescription = stringResource(R.string.edit)
                )
            }
        )
        DropdownMenuItem(
            text = {
                Text(
                    stringResource(R.string.delete),
                    color = MaterialTheme.colorScheme.error
                )
            },
            onClick = onDeleteClick,
            leadingIcon = {
                Icon(
                    imageVector = AppIcons.Delete,
                    contentDescription = stringResource(R.string.delete),
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
                    text = stringResource(R.string.confirm),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.cancel))
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