package io.floriax.medschedule.feature.medicationlog.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import io.floriax.medschedule.core.domain.enums.MedicationLogType
import io.floriax.medschedule.core.domain.enums.MedicationState
import io.floriax.medschedule.core.domain.model.MedicationLog
import io.floriax.medschedule.core.domain.model.TakenMedication
import io.floriax.medschedule.feature.medicationlog.R
import io.floriax.medschedule.shared.designsystem.icon.AppIcons
import io.floriax.medschedule.shared.designsystem.theme.AppTheme
import io.floriax.medschedule.shared.ui.component.LoadingIndicator
import io.floriax.medschedule.shared.ui.component.ManualTag
import io.floriax.medschedule.shared.ui.component.MedScheduleTopAppBar
import io.floriax.medschedule.shared.ui.extension.formatLocalDateTime
import kotlinx.coroutines.flow.flowOf

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/26
 */
@Composable
fun MedicationLogRoute(
    onCreateMedicationLogClick: () -> Unit,
    onMedicationLogClick: (MedicationLog) -> Unit,
    viewModel: MedicationLogViewModel = hiltViewModel()
) {

    val medicationLogPagingItems = viewModel.pagedMedicationLogs.collectAsLazyPagingItems()

    MedicationLogScreen(
        medicationLogPagingItems = medicationLogPagingItems,
        onCreateMedicationLogClick = onCreateMedicationLogClick,
        onMedicationLogClick = onMedicationLogClick
    )
}

@Composable
private fun MedicationLogScreen(
    medicationLogPagingItems: LazyPagingItems<MedicationLog>,
    onCreateMedicationLogClick: () -> Unit,
    onMedicationLogClick: (MedicationLog) -> Unit,
    modifier: Modifier = Modifier
) {

    val listState = rememberLazyListState()
    val showFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset < 10
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MedicationLogTopBar()
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showFab,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
            ) {
                FloatingActionButton(
                    onClick = onCreateMedicationLogClick
                ) {
                    Icon(
                        imageVector = AppIcons.Add,
                        contentDescription = stringResource(R.string.screen_medication_log_add_medication_log)
                    )
                }
            }
        }
    ) { paddingValues ->
        when {
            medicationLogPagingItems.loadState.refresh == LoadState.Loading ->
                LoadingIndicator(modifier = modifier.fillMaxSize())

            medicationLogPagingItems.itemCount == 0 ->
                EmptyMedicationLogList(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(top = 128.dp)
                )

            else -> {
                MedicationLogList(
                    listState = listState,
                    medicationLogPagingItems = medicationLogPagingItems,
                    onMedicationLogClick = onMedicationLogClick,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun MedicationLogTopBar(
    modifier: Modifier = Modifier
) {
    MedScheduleTopAppBar(
        titleRes = R.string.screen_medication_log_title,
        subtitleRes = R.string.screen_medication_log_subtitle,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun EmptyMedicationLogList(
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
            imageVector = AppIcons.HistorySlimBorder,
            contentDescription = null,
            modifier = Modifier.size(96.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.screen_medication_log_empty),
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun MedicationLogList(
    listState: LazyListState,
    medicationLogPagingItems: LazyPagingItems<MedicationLog>,
    onMedicationLogClick: (MedicationLog) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            count = medicationLogPagingItems.itemCount,
            key = medicationLogPagingItems.itemKey { item -> item.id },
        ) { index ->
            val item = medicationLogPagingItems[index]
            if (item != null) {
                MedicationLogCard(
                    medicationLog = item,
                    onMedicationLogClick = { onMedicationLogClick(item) },
                )
            }
        }
    }
}

@Composable
private fun MedicationLogCard(
    medicationLog: MedicationLog,
    onMedicationLogClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val stateVisuals = getVisualsForState(medicationLog.state)

    ElevatedCard(
        onClick = onMedicationLogClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(stateVisuals.color)
            )
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = medicationLog.medicationTime.formatLocalDateTime(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (medicationLog.type == MedicationLogType.MANUAL) {
                            ManualTag()
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        medicationLog.takenMedications.forEach { takenMedication ->
                            TakenMedicationItem(takenMedication)
                        }
                    }
                    val notes = medicationLog.notes
                    if (!notes.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        NotesSection(notes = notes)
                    }
                }
            }
        }
    }
}

@Composable
private fun getVisualsForState(state: MedicationState): StateVisuals =
    when (state) {
        MedicationState.PENDING -> StateVisuals(MaterialTheme.colorScheme.tertiary)
        MedicationState.TAKEN -> StateVisuals(MaterialTheme.colorScheme.primary)
        MedicationState.SKIPPED -> StateVisuals(MaterialTheme.colorScheme.onSurfaceVariant)
        MedicationState.MISSED -> StateVisuals(MaterialTheme.colorScheme.error)
        else -> StateVisuals(MaterialTheme.colorScheme.outline)
    }

data class StateVisuals(val color: Color)

@Composable
private fun TakenMedicationItem(
    takenMedication: TakenMedication,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = takenMedication.medication.name,
            modifier = Modifier
                .weight(1f)
                .basicMarquee(),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "${takenMedication.dose.toPlainString()} ${takenMedication.medication.doseUnit}",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun NotesSection(notes: String) {
    Row(verticalAlignment = Alignment.Top) {
        Text(
            text = stringResource(R.string.screen_medication_log_card_notes_prefix),
            color = MaterialTheme.colorScheme.outline,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = notes,
            color = MaterialTheme.colorScheme.outline,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MedicationLogScreenPreview() {
    val medicationLogs = PagingData.empty<MedicationLog>()
    val medicationLogsFlow = flowOf(medicationLogs)
    AppTheme {
        MedicationLogScreen(
            medicationLogPagingItems = medicationLogsFlow.collectAsLazyPagingItems(),
            onCreateMedicationLogClick = {},
            onMedicationLogClick = {}
        )
    }
}