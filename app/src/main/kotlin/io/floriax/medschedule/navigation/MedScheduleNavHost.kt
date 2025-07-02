package io.floriax.medschedule.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import io.floriax.medschedule.feature.medication.navigation.addMedicationScreen
import io.floriax.medschedule.feature.medication.navigation.editMedicationScreen
import io.floriax.medschedule.feature.medication.navigation.navigateToAddMedication
import io.floriax.medschedule.feature.medication.navigation.navigateToEditMedication
import io.floriax.medschedule.feature.medicationrecord.navigation.createMedicationRecordScreen
import io.floriax.medschedule.feature.medicationrecord.navigation.navigateToCreateMedicationRecord
import io.floriax.medschedule.navigation.main.MainRoute
import io.floriax.medschedule.navigation.main.mainScreen

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
@Composable
fun MedScheduleNavHost(
    appState: MedScheduleAppState,
    modifier: Modifier = Modifier,
) {

    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = MainRoute,
        modifier = modifier,
    ) {
        mainScreen(
            onAddMedicationClick = navController::navigateToAddMedication,
            onEditMedicationClick = navController::navigateToEditMedication,
            onCreateMedicationRecordClick = navController::navigateToCreateMedicationRecord
        )

        addMedicationScreen(
            onBackClick = navController::popBackStack
        )

        editMedicationScreen(
            onBackClick = navController::popBackStack
        )

        createMedicationRecordScreen(
            onBackClick = navController::popBackStack
        )
    }
}