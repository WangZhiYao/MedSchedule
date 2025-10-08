package io.floriax.medschedule.feature.medicationplan.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import io.floriax.medschedule.core.domain.model.MedicationIntake
import io.floriax.medschedule.core.domain.model.MedicationPlan
import io.floriax.medschedule.core.domain.model.MedicationSchedule
import io.floriax.medschedule.feature.medicationplan.R
import io.floriax.medschedule.shared.designsystem.icon.AppIcons
import io.floriax.medschedule.shared.ui.component.BackButton
import io.floriax.medschedule.shared.ui.component.DeleteButton
import io.floriax.medschedule.shared.ui.component.EditButton
import io.floriax.medschedule.shared.ui.component.ErrorIndicator
import io.floriax.medschedule.shared.ui.component.LoadingIndicator
import io.floriax.medschedule.shared.ui.extension.collectSideEffect
import io.floriax.medschedule.shared.ui.extension.collectState
import io.floriax.medschedule.shared.ui.extension.formatLocalized
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import io.floriax.medschedule.shared.ui.R as sharedUiR

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/10/8
 */
@Composable
fun MedicationPlanDetailRoute(
    onBackClick: () -> Unit,
    onEditClick: (Long) -> Unit,
    viewModel: MedicationPlanDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.collectState()

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            DeletePlanSuccess -> onBackClick()
            DeletePlanFailure -> snackbarHostState.showSnackbar(
                message = context.getString(R.string.screen_medication_plan_detail_delete_failure)
            )
        }
    }

    if (uiState.showDeleteDialog) {
        DeleteConfirmationDialog(
            planName = uiState.medicationPlan?.name ?: "",
            onDismissRequest = { viewModel.toggleDeleteDialog(false) },
            onConfirmClick = { viewModel.attemptDeletePlan() }
        )
    }

    MedicationPlanDetailScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBackClick = onBackClick,
        onEditClick = { uiState.medicationPlan?.let { onEditClick(it.id) } },
        onDeleteClick = { viewModel.toggleDeleteDialog(true) },
        onStatusChange = viewModel::onStatusChange
    )
}

@Composable
private fun MedicationPlanDetailScreen(
    uiState: MedicationPlanDetailUiState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onStatusChange: (Boolean) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MedicationPlanDetailTopBar(
                planName = uiState.medicationPlan?.name,
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
                uiState.loading -> LoadingIndicator(modifier = Modifier.fillMaxSize())
                uiState.error -> ErrorIndicator(modifier = Modifier.fillMaxSize())
                uiState.medicationPlan != null -> {
                    MedicationPlanDetailContent(
                        plan = uiState.medicationPlan,
                        onStatusChange = onStatusChange
                    )
                }
            }
        }
    }
}

@Composable
private fun MedicationPlanDetailContent(
    plan: MedicationPlan,
    onStatusChange: (Boolean) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            StatusCard(plan.active, onStatusChange)
        }

        item {
            ScheduleCard(plan.schedule)
        }

        item {
            IntakesCard(plan.schedule.allIntakes)
        }

        if (!plan.notes.isNullOrBlank()) {
            item {
                NotesCard(plan.notes!!)
            }
        }
    }
}

@Composable
private fun StatusCard(active: Boolean, onStatusChange: (Boolean) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.screen_medication_plan_detail_status),
                style = MaterialTheme.typography.titleMedium
            )
            Switch(checked = active, onCheckedChange = onStatusChange)
        }
    }
}

@Composable
private fun ScheduleCard(schedule: MedicationSchedule) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.screen_medication_plan_detail_schedule_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            val scheduleSummary = schedule.toSummary()

            DetailItem(
                icon = AppIcons.CalendarClock,
                title = scheduleSummary.first,
                subtitle = scheduleSummary.second
            )
        }
    }
}

@Composable
private fun IntakesCard(intakes: List<MedicationIntake>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.screen_medication_plan_detail_intakes_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            intakes.forEachIndexed { index, intake ->
                Text(
                    text = intake.time.formatLocalized(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = if (index > 0) 12.dp else 0.dp, bottom = 4.dp)
                )
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    intake.medicationDoses.forEach { dose ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = dose.medication.name,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "${dose.dose.toPlainString()} ${dose.medication.doseUnit}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotesCard(notes: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.screen_medication_plan_detail_notes_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Text(text = notes, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun DetailItem(icon: ImageVector, title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun MedicationSchedule.toSummary(): Pair<String, String> {
    return when (this) {
        is MedicationSchedule.OneTime -> {
            stringResource(R.string.screen_medication_plan_summary_one_time_title) to date.formatLocalized()
        }

        is MedicationSchedule.Repetitive.Daily -> {
            stringResource(R.string.screen_medication_plan_summary_daily) to formatRepetitiveRange(
                startDate,
                endDate
            )
        }

        is MedicationSchedule.Repetitive.Weekly -> {
            val days = daysOfWeek.sorted()
                .joinToString { it.getDisplayName(TextStyle.NARROW, Locale.getDefault()) }
            stringResource(
                R.string.screen_medication_plan_summary_weekly,
                days
            ) to formatRepetitiveRange(startDate, endDate)
        }

        is MedicationSchedule.Repetitive.Interval -> {
            stringResource(
                R.string.screen_medication_plan_summary_interval,
                intervalDays
            ) to formatRepetitiveRange(startDate, endDate)
        }

        is MedicationSchedule.Repetitive.CustomCycle -> {
            stringResource(
                R.string.screen_medication_plan_summary_custom_cycle,
                cycleLengthInDays
            ) to formatRepetitiveRange(startDate, endDate)
        }
    }
}

@Composable
private fun formatRepetitiveRange(start: LocalDate, end: LocalDate?): String {
    val startFormatted = start.formatLocalized()
    val endFormatted = end?.formatLocalized()
        ?: stringResource(R.string.screen_medication_plan_detail_range_end_forever)
    return stringResource(
        R.string.screen_medication_plan_detail_range_format,
        startFormatted,
        endFormatted
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MedicationPlanDetailTopBar(
    planName: String?,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = planName ?: stringResource(R.string.screen_medication_plan_detail_title)
            )
        },
        navigationIcon = { BackButton(onClick = onBackClick) },
        actions = {
            EditButton(onClick = onEditClick)
            DeleteButton(onClick = onDeleteClick)
        }
    )
}

@Composable
private fun DeleteConfirmationDialog(
    planName: String,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(R.string.screen_medication_plan_detail_delete_dialog_title)) },
        text = {
            Text(
                text = stringResource(
                    R.string.screen_medication_plan_detail_delete_dialog_text,
                    planName
                )
            )
        },
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
        }
    )
}
