package io.floriax.medschedule.feature.medicationplan.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import io.floriax.medschedule.core.domain.model.MedicationPlan
import io.floriax.medschedule.core.domain.model.MedicationSchedule
import io.floriax.medschedule.feature.medicationplan.R
import io.floriax.medschedule.shared.designsystem.icon.AppIcons
import io.floriax.medschedule.shared.ui.component.MedScheduleTopAppBar
import io.floriax.medschedule.shared.ui.extension.collectState
import io.floriax.medschedule.shared.ui.extension.formatLocalized
import java.time.format.TextStyle
import java.util.Locale

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/26
 */
@Composable
fun MedicationPlanRoute(
    onCreateMedicationPlanClick: () -> Unit,
    onMedicationPlanClick: (Long) -> Unit,
    viewModel: MedicationPlanViewModel = hiltViewModel()
) {

    val uiState by viewModel.collectState()

    MedicationPlanScreen(
        uiState = uiState,
        onCreateMedicationPlanClick = onCreateMedicationPlanClick,
        onMedicationPlanClick = onMedicationPlanClick
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MedicationPlanScreen(
    uiState: MedicationPlanUiState,
    onCreateMedicationPlanClick: () -> Unit,
    onMedicationPlanClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MedicationPlanTopBar()
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateMedicationPlanClick
            ) {
                Icon(
                    imageVector = AppIcons.Add,
                    contentDescription = stringResource(R.string.screen_medication_plan_create_medication_plan)
                )
            }
        }
    ) { paddingValues ->
        if (uiState.activePlans.isEmpty() && uiState.inactivePlans.isEmpty()) {
            EmptyMedicationPlanList(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(top = 128.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (uiState.activePlans.isNotEmpty()) {
                    stickyHeader {
                        ListHeader(title = stringResource(R.string.screen_medication_plan_header_active))
                    }
                    items(
                        items = uiState.activePlans,
                        key = { plan -> plan.id }
                    ) { plan ->
                        MedicationPlanCard(
                            plan = plan,
                            onClick = { onMedicationPlanClick(plan.id) },
                        )
                    }
                }

                if (uiState.inactivePlans.isNotEmpty()) {
                    stickyHeader {
                        ListHeader(title = stringResource(R.string.screen_medication_plan_header_archived))
                    }
                    items(
                        items = uiState.inactivePlans,
                        key = { plan -> plan.id }
                    ) { plan ->
                        MedicationPlanCard(
                            plan = plan,
                            onClick = { onMedicationPlanClick(plan.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MedicationPlanTopBar(
    modifier: Modifier = Modifier
) {
    MedScheduleTopAppBar(
        titleRes = R.string.screen_medication_plan_title,
        subtitleRes = R.string.screen_medication_plan_subtitle,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun ListHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 4.dp)
    )
}

@Composable
private fun EmptyMedicationPlanList(
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
            imageVector = AppIcons.CalendarClockSlimBorder,
            contentDescription = null,
            modifier = Modifier.size(96.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.screen_medication_plan_empty),
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.screen_medication_plan_click_to_add),
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun MedicationPlanCard(
    plan: MedicationPlan,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Box {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = plan.name,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = plan.schedule.toSummary(), style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                val medications =
                    plan.schedule.allIntakes.flatMap { it.medicationDoses }
                        .map { it.medication.name }
                        .distinct()
                Text(
                    text = medications.joinToString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun MedicationSchedule.toSummary(): String {
    return when (this) {
        is MedicationSchedule.OneTime -> stringResource(
            R.string.screen_medication_plan_summary_one_time,
            date.formatLocalized()
        )

        is MedicationSchedule.Repetitive.Daily -> stringResource(R.string.screen_medication_plan_summary_daily)
        is MedicationSchedule.Repetitive.Weekly -> stringResource(
            R.string.screen_medication_plan_summary_weekly,
            daysOfWeek.joinToString { it.getDisplayName(TextStyle.NARROW, Locale.getDefault()) })

        is MedicationSchedule.Repetitive.Interval -> stringResource(
            R.string.screen_medication_plan_summary_interval,
            intervalDays
        )

        is MedicationSchedule.Repetitive.CustomCycle -> stringResource(
            R.string.screen_medication_plan_summary_custom_cycle,
            cycleLengthInDays
        )
    }
}
