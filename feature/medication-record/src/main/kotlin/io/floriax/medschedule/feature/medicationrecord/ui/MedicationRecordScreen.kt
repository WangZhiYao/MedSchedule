package io.floriax.medschedule.feature.medicationrecord.ui

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import io.floriax.medschedule.core.domain.enums.MedicationRecordType
import io.floriax.medschedule.core.domain.enums.MedicationState
import io.floriax.medschedule.core.domain.model.MedicationRecord
import io.floriax.medschedule.feature.medicationrecord.R
import io.floriax.medschedule.shared.designsystem.component.MedScheduleTopAppBar
import io.floriax.medschedule.shared.designsystem.icon.AppIcons
import io.floriax.medschedule.shared.ui.MedScheduleLoadingIndicator
import io.floriax.medschedule.shared.ui.extension.formatLocalDateTime

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/26
 */
@Composable
fun MedicationRecordRoute(
    viewModel: MedicationRecordViewModel = hiltViewModel()
) {

    val medicationRecordPagingItems = viewModel.pagedMedicationRecords.collectAsLazyPagingItems()

    MedicationRecordScreen(
        medicationRecordPagingItems = medicationRecordPagingItems
    )
}

@Composable
private fun MedicationRecordScreen(
    medicationRecordPagingItems: LazyPagingItems<MedicationRecord>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MedicationRecordTopBar()
        }
    ) { paddingValues ->
        when {
            medicationRecordPagingItems.loadState.refresh == LoadState.Loading ->
                MedScheduleLoadingIndicator(modifier = modifier.fillMaxSize())

            medicationRecordPagingItems.itemCount == 0 ->
                EmptyMedicationRecordList(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(top = 128.dp)
                )

            else -> {
                MedicationRecordList(
                    medicationRecordPagingItems = medicationRecordPagingItems,
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
    medicationRecordPagingItems: LazyPagingItems<MedicationRecord>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    ) {
        items(
            count = medicationRecordPagingItems.itemCount,
            key = medicationRecordPagingItems.itemKey { item -> item.id },
        ) { index ->
            val item = medicationRecordPagingItems[index]
            if (item != null) {
                MedicationRecordCard(
                    medicationRecord = item
                )
            }
        }
    }
}

@Composable
private fun MedicationRecordCard(
    medicationRecord: MedicationRecord,
    modifier: Modifier = Modifier
) {

    val (icon, color) = getVisualsForState(medicationRecord.state)
    ElevatedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = medicationRecord.state.name,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = medicationRecord.medicationTime.formatLocalDateTime(),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                if (medicationRecord.type == MedicationRecordType.MANUAL) {
                    Text(
                        text = stringResource(R.string.screen_medication_record_card_tag_manual),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier.padding(start = 36.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                medicationRecord.takenMedications.forEach { takenMed ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = takenMed.medication.name,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "${takenMed.dose} ${takenMed.medication.doseUnit}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            if (medicationRecord.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.padding(start = 36.dp)
                ) {
                    Text(
                        text = stringResource(
                            R.string.screen_medication_record_card_notes_prefix,
                            medicationRecord.notes
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun getVisualsForState(state: MedicationState): Pair<ImageVector, Color> =
    when (state) {
        MedicationState.PENDING -> AppIcons.RadioButtonUnchecked to MaterialTheme.colorScheme.tertiary
        MedicationState.TAKEN -> AppIcons.CheckCircle to MaterialTheme.colorScheme.primary
        MedicationState.SKIPPED -> AppIcons.RemoveCircle to MaterialTheme.colorScheme.onSurfaceVariant
        MedicationState.MISSED -> AppIcons.Error to MaterialTheme.colorScheme.error
        else -> AppIcons.HelpOutline to MaterialTheme.colorScheme.outline
    }