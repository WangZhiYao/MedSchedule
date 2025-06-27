package io.floriax.medschedule.feature.medication.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.floriax.medschedule.feature.medication.R
import io.floriax.medschedule.shared.designsystem.component.MedScheduleTopAppBar
import io.floriax.medschedule.shared.designsystem.icon.AppIcons
import io.floriax.medschedule.shared.designsystem.theme.AppTheme

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/26
 */
@Composable
fun MedicineCabinetRoute(
    onAddMedicationClick: () -> Unit
) {

    MedicineCabinetScreen(
        onAddMedicationClick = onAddMedicationClick
    )
}

@Composable
private fun MedicineCabinetScreen(
    onAddMedicationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MedicineCabinetTopBar()
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddMedicationClick) {
                Icon(
                    imageVector = AppIcons.Add,
                    contentDescription = null
                )
            }
        }
    ) { paddingValues ->
        MedicineCabinetContent(
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MedicineCabinetTopBar(
    modifier: Modifier = Modifier
) {
    MedScheduleTopAppBar(
        titleRes = R.string.screen_medicine_cabinet_title,
        subtitleRes = R.string.screen_medicine_cabinet_subtitle,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun MedicineCabinetContent(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {

    }
}

@Preview(showBackground = true)
@Composable
private fun MedicineCabinetScreenPreview() {
    AppTheme {
        MedicineCabinetScreen(
            onAddMedicationClick = {}
        )
    }
}