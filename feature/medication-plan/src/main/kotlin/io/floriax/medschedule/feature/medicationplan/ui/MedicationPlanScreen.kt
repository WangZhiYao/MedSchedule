package io.floriax.medschedule.feature.medicationplan.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.floriax.medschedule.feature.medicationplan.R
import io.floriax.medschedule.shared.designsystem.icon.AppIcons
import io.floriax.medschedule.shared.ui.component.MedScheduleTopAppBar

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/26
 */
@Composable
fun MedicationPlanRoute(
    onCreateMedicationPlanClick: () -> Unit
) {

    MedicationPlanScreen(
        onCreateMedicationPlanClick = onCreateMedicationPlanClick
    )
}

@Composable
private fun MedicationPlanScreen(
    onCreateMedicationPlanClick: () -> Unit,
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