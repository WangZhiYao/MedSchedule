package io.floriax.medschedule.feature.medicationplan.ui.create.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.floriax.medschedule.core.domain.enums.MedicationScheduleType
import io.floriax.medschedule.feature.medicationplan.R
import io.floriax.medschedule.feature.medicationplan.ui.create.CreateMedicationPlanUiState
import io.floriax.medschedule.shared.designsystem.theme.AppTheme
import io.floriax.medschedule.shared.ui.extension.formatLocalized
import io.floriax.medschedule.shared.ui.extension.label
import java.time.format.TextStyle
import java.util.Locale

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/10/5
 */
@Composable
fun SaveStep(
    uiState: CreateMedicationPlanUiState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.screen_create_medication_plan_save_summary_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }

        item {
            SummaryCard(title = stringResource(R.string.screen_create_medication_plan_basic_info_title)) {
                Text(
                    text = uiState.name,
                    style = MaterialTheme.typography.titleMedium
                )
                if (uiState.notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.notes,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            SummaryCard(title = stringResource(R.string.screen_create_medication_plan_schedule_type_title)) {
                Text(
                    text = uiState.selectedScheduleType.label(),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                when (uiState.selectedScheduleType) {
                    MedicationScheduleType.ONE_TIME -> {
                        Text(
                            stringResource(
                                R.string.screen_create_medication_plan_save_date,
                                uiState.oneTimeScheduleDate.formatLocalized()
                            )
                        )
                    }

                    MedicationScheduleType.DAILY -> {
                        Text(
                            stringResource(
                                R.string.screen_create_medication_plan_save_from_to,
                                uiState.startDate.formatLocalized(),
                                uiState.endDate?.formatLocalized()
                                    ?: stringResource(R.string.screen_create_medication_plan_save_forever)
                            )
                        )
                    }

                    MedicationScheduleType.WEEKLY -> {
                        Text(
                            stringResource(
                                R.string.screen_create_medication_plan_save_from_to,
                                uiState.startDate.formatLocalized(),
                                uiState.endDate?.formatLocalized()
                                    ?: stringResource(R.string.screen_create_medication_plan_save_forever)
                            )
                        )
                        Text(
                            stringResource(
                                R.string.screen_create_medication_plan_save_on,
                                uiState.weeklySelectedDays.joinToString {
                                    it.getDisplayName(
                                        TextStyle.SHORT,
                                        Locale.getDefault()
                                    )
                                }
                            )
                        )
                    }

                    MedicationScheduleType.INTERVAL -> {
                        Text(
                            stringResource(
                                R.string.screen_create_medication_plan_save_from_to,
                                uiState.startDate.formatLocalized(),
                                uiState.endDate?.formatLocalized()
                                    ?: stringResource(R.string.screen_create_medication_plan_save_forever)
                            )
                        )
                        Text(
                            stringResource(
                                R.string.screen_create_medication_plan_save_every_days,
                                uiState.intervalDays
                            )
                        )
                    }

                    MedicationScheduleType.CUSTOM_CYCLE -> {
                        Text(
                            stringResource(
                                R.string.screen_create_medication_plan_save_from_to,
                                uiState.startDate.formatLocalized(),
                                uiState.endDate?.formatLocalized()
                                    ?: stringResource(R.string.screen_create_medication_plan_save_forever)
                            )
                        )
                        Text(
                            stringResource(
                                R.string.screen_create_medication_plan_save_cycle,
                                uiState.customCycleDaysOn,
                                uiState.customCycleDaysOff
                            )
                        )
                    }

                    else -> {}
                }
            }
        }

        item {
            Text(
                text = stringResource(R.string.screen_create_medication_plan_dosage_title),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(uiState.intakes) { intake ->
            SummaryCard(
                title = stringResource(
                    R.string.screen_create_medication_plan_save_intake_at,
                    intake.time.formatLocalized()
                )
            ) {
                intake.doses.forEachIndexed { index, dose ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = dose.medication?.name
                                ?: stringResource(R.string.screen_create_medication_plan_save_not_selected),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${dose.dose} ${dose.medication?.doseUnit ?: ""}"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SummaryStepPreview() {
    AppTheme {
        SaveStep(
            uiState = CreateMedicationPlanUiState(
                name = "Test",
                notes = "Test",
            )
        )
    }
}