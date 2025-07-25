package io.floriax.medschedule.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import io.floriax.medschedule.feature.medication.navigation.addMedicationScreen
import io.floriax.medschedule.feature.medication.navigation.editMedicationScreen
import io.floriax.medschedule.feature.medication.navigation.medicationDetailScreen
import io.floriax.medschedule.feature.medication.navigation.navigateToAddMedication
import io.floriax.medschedule.feature.medication.navigation.navigateToEditMedication
import io.floriax.medschedule.feature.medication.navigation.navigateToMedicationDetail
import io.floriax.medschedule.feature.medicationlog.navigation.createMedicationLogScreen
import io.floriax.medschedule.feature.medicationlog.navigation.medicationLogDetailScreen
import io.floriax.medschedule.feature.medicationlog.navigation.navigateToCreateMedicationLog
import io.floriax.medschedule.feature.medicationlog.navigation.navigateToMedicationLogDetail
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
            onMedicationClick = navController::navigateToMedicationDetail,
            onCreateMedicationLogClick = navController::navigateToCreateMedicationLog,
            onMedicationLogClick = { medicationLog ->
                navController.navigateToMedicationLogDetail(medicationLog.id)
            }
        )

        addMedicationScreen(
            onBackClick = navController::popBackStack
        )

        medicationDetailScreen(
            onBackClick = navController::popBackStack,
            onEditClick = navController::navigateToEditMedication,
            onMedicationLogClick = { medicationLog ->
                navController.navigateToMedicationLogDetail(medicationLog.id)
            },
        )

        editMedicationScreen(
            onBackClick = navController::popBackStack
        )

        createMedicationLogScreen(
            onBackClick = navController::popBackStack,
            onAddMedicationClick = navController::navigateToAddMedication
        )

        medicationLogDetailScreen(
            onBackClick = navController::popBackStack,
            onEditClick = { /**TODO*/ }
        )
    }
}