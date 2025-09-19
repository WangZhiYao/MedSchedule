package io.floriax.medschedule.feature.medication.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.feature.medication.R
import io.floriax.medschedule.shared.designsystem.icon.AppIcons
import io.floriax.medschedule.shared.designsystem.theme.AppTheme
import io.floriax.medschedule.shared.ui.component.ErrorIndicator
import io.floriax.medschedule.shared.ui.component.LoadingIndicator
import io.floriax.medschedule.shared.ui.component.MedScheduleTopAppBar
import io.floriax.medschedule.shared.ui.extension.collectState
import kotlinx.coroutines.flow.flowOf

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/26
 */
@Composable
fun MedicationsRoute(
    onAddMedicationClick: () -> Unit,
    onMedicationClick: (Medication) -> Unit,
    viewModel: MedicationsViewModel = hiltViewModel(),
) {

    val state by viewModel.collectState()

    val medicationPagingItems = viewModel.pagedMedications.collectAsLazyPagingItems()

    MedicationsScreen(
        state = state,
        medicationPagingItems = medicationPagingItems,
        onAddMedicationClick = onAddMedicationClick,
        onMedicationClick = onMedicationClick
    )
}

@Composable
private fun MedicationsScreen(
    state: MedicationsUiState,
    medicationPagingItems: LazyPagingItems<Medication>,
    onAddMedicationClick: () -> Unit,
    onMedicationClick: (Medication) -> Unit,
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
            MedicationsTopBar()
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showFab,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
            ) {
                FloatingActionButton(onClick = onAddMedicationClick) {
                    Icon(
                        imageVector = AppIcons.Add,
                        contentDescription = stringResource(R.string.screen_medications_add_medication)
                    )
                }
            }
        }
    ) { paddingValues ->
        MedicationsContent(
            state = state,
            listState = listState,
            medicationPagingItems = medicationPagingItems,
            onMedicationClick = onMedicationClick,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MedicationsTopBar(
    modifier: Modifier = Modifier
) {
    MedScheduleTopAppBar(
        titleRes = R.string.screen_medications_title,
        subtitleRes = R.string.screen_medications_subtitle,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun MedicationsContent(
    state: MedicationsUiState,
    listState: LazyListState,
    medicationPagingItems: LazyPagingItems<Medication>,
    onMedicationClick: (Medication) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        medicationPagingItems.loadState.refresh == LoadState.Loading ->
            LoadingIndicator(modifier = modifier.fillMaxSize())

        medicationPagingItems.itemCount == 0 ->
            EmptyMedicationList(modifier = modifier.padding(top = 128.dp))

        state.error -> ErrorIndicator(modifier.fillMaxSize())

        else -> {
            MedicationList(
                state = listState,
                medicationPagingItems = medicationPagingItems,
                onMedicationClick = onMedicationClick,
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
            imageVector = AppIcons.MedicationSlimBorder,
            contentDescription = null,
            modifier = Modifier.size(96.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.screen_medications_empty_medication),
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.screen_medications_click_to_add_medication),
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun MedicationList(
    state: LazyListState,
    medicationPagingItems: LazyPagingItems<Medication>,
    onMedicationClick: (Medication) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = state,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            count = medicationPagingItems.itemCount,
            key = medicationPagingItems.itemKey { item -> item.id },
        ) { index ->
            val item = medicationPagingItems[index]
            if (item != null) {
                MedicationCard(
                    medication = item,
                    onMedicationClick = { onMedicationClick(item) }
                )
            }
        }
    }
}

@Composable
private fun MedicationCard(
    medication: Medication,
    onMedicationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        onClick = onMedicationClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = medication.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .basicMarquee(),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = medication.notes.ifNullOrBlank {
                        stringResource(R.string.screen_medications_no_notes)
                    },
                    color = MaterialTheme.colorScheme.outline,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    minLines = 2,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val stock = medication.stock
                    if (stock != null) {
                        Row {
                            Text(
                                text = stock.toPlainString(),
                                modifier = Modifier.alignBy(LastBaseline),
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.size(4.dp))
                            Text(
                                text = medication.doseUnit,
                                modifier = Modifier.alignBy(LastBaseline)
                            )
                        }
                    } else {
                        Text(
                            text = stringResource(R.string.screen_medications_stock_not_set),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Icon(
                        imageVector = AppIcons.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
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
            onMedicationClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MedicationsScreenPreview() {
    AppTheme {
        val pagingData = PagingData.from(emptyList<Medication>())
        val medicationPagingItems = flowOf(pagingData).collectAsLazyPagingItems()
        MedicationsScreen(
            state = MedicationsUiState(),
            medicationPagingItems = medicationPagingItems,
            onAddMedicationClick = {},
            onMedicationClick = {}
        )
    }
}