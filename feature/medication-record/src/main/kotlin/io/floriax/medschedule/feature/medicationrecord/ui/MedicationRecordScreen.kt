package io.floriax.medschedule.feature.medicationrecord.ui

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
import io.floriax.medschedule.core.domain.enums.MedicationRecordType
import io.floriax.medschedule.core.domain.enums.MedicationState
import io.floriax.medschedule.core.domain.model.MedicationRecord
import io.floriax.medschedule.core.domain.model.TakenMedication
import io.floriax.medschedule.feature.medicationrecord.R
import io.floriax.medschedule.shared.designsystem.component.MedScheduleTopAppBar
import io.floriax.medschedule.shared.designsystem.icon.AppIcons
import io.floriax.medschedule.shared.designsystem.theme.AppTheme
import io.floriax.medschedule.shared.ui.LoadingIndicator
import io.floriax.medschedule.shared.ui.ManualTag
import io.floriax.medschedule.shared.ui.extension.formatLocalDateTime
import kotlinx.coroutines.flow.flowOf

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/26
 */
@Composable
fun MedicationRecordRoute(
    onCreateMedicationRecordClick: () -> Unit,
    onMedicationRecordClick: (MedicationRecord) -> Unit,
    viewModel: MedicationRecordViewModel = hiltViewModel()
) {

    val medicationRecordPagingItems = viewModel.pagedMedicationRecords.collectAsLazyPagingItems()

    MedicationRecordScreen(
        medicationRecordPagingItems = medicationRecordPagingItems,
        onCreateMedicationRecordClick = onCreateMedicationRecordClick,
        onMedicationRecordClick = onMedicationRecordClick
    )
}

@Composable
private fun MedicationRecordScreen(
    medicationRecordPagingItems: LazyPagingItems<MedicationRecord>,
    onCreateMedicationRecordClick: () -> Unit,
    onMedicationRecordClick: (MedicationRecord) -> Unit,
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
            MedicationRecordTopBar()
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showFab,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
            ) {
                FloatingActionButton(
                    onClick = onCreateMedicationRecordClick
                ) {
                    Icon(
                        imageVector = AppIcons.Add,
                        contentDescription = stringResource(R.string.screen_medication_record_add_medication_record)
                    )
                }
            }
        }
    ) { paddingValues ->
        when {
            medicationRecordPagingItems.loadState.refresh == LoadState.Loading ->
                LoadingIndicator(modifier = modifier.fillMaxSize())

            medicationRecordPagingItems.itemCount == 0 ->
                EmptyMedicationRecordList(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(top = 128.dp)
                )

            else -> {
                MedicationRecordList(
                    listState = listState,
                    medicationRecordPagingItems = medicationRecordPagingItems,
                    onMedicationRecordClick = onMedicationRecordClick,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun MedicationRecordTopBar(
    modifier: Modifier = Modifier
) {
    MedScheduleTopAppBar(
        titleRes = R.string.screen_medication_record_title,
        subtitleRes = R.string.screen_medication_record_subtitle,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun EmptyMedicationRecordList(
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
            text = stringResource(R.string.screen_medication_record_empty),
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun MedicationRecordList(
    listState: LazyListState,
    medicationRecordPagingItems: LazyPagingItems<MedicationRecord>,
    onMedicationRecordClick: (MedicationRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            count = medicationRecordPagingItems.itemCount,
            key = medicationRecordPagingItems.itemKey { item -> item.id },
        ) { index ->
            val item = medicationRecordPagingItems[index]
            if (item != null) {
                MedicationRecordCard(
                    medicationRecord = item,
                    onMedicationRecordClick = { onMedicationRecordClick(item) },
                )
            }
        }
    }
}

@Composable
private fun MedicationRecordCard(
    medicationRecord: MedicationRecord,
    onMedicationRecordClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val stateVisuals = getVisualsForState(medicationRecord.state)

    ElevatedCard(
        onClick = onMedicationRecordClick,
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
                            text = medicationRecord.medicationTime.formatLocalDateTime(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (medicationRecord.type == MedicationRecordType.MANUAL) {
                            ManualTag()
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        medicationRecord.takenMedications.forEach { takenMedication ->
                            TakenMedicationItem(takenMedication)
                        }
                    }
                    val notes = medicationRecord.notes
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
            text = "${takenMedication.dose} ${takenMedication.medication.doseUnit}",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun NotesSection(notes: String) {
    Row(verticalAlignment = Alignment.Top) {
        Text(
            text = stringResource(R.string.screen_medication_record_card_notes_prefix),
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
private fun MedicationRecordScreenPreview() {
    val medicationRecords = PagingData.empty<MedicationRecord>()
    val medicationRecordsFlow = flowOf(medicationRecords)
    AppTheme {
        MedicationRecordScreen(
            medicationRecordPagingItems = medicationRecordsFlow.collectAsLazyPagingItems(),
            onCreateMedicationRecordClick = {},
            onMedicationRecordClick = {}
        )
    }
}