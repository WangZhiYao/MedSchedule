package io.floriax.medschedule.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.floriax.medschedule.ui.home.HomeScreen
import io.floriax.medschedule.ui.medication.MedicationListScreen
import io.floriax.medschedule.ui.medication.add.AddMedicationScreen
import io.floriax.medschedule.ui.medication.edit.EditMedicationScreen
import io.floriax.medschedule.ui.medication.record.add.AddMedicationRecordScreen

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
        startDestination = Route.Home,
        modifier = modifier,
    ) {

        composable<Route.Home> {
            HomeScreen(
                onMedicationClick = { navController.navigate(Route.MedicationList) },
                onAddMedicationRecordClick = { navController.navigate(Route.AddMedicationRecord) }
            )
        }

        composable<Route.MedicationList> {
            MedicationListScreen(
                onBackClick = { navController.popBackStack() },
                onAddMedicationClick = { navController.navigate(Route.AddMedication) },
                onEditMedicationClick = { medication ->
                    navController.navigate(Route.EditMedication(medication.id))
                },
            )
        }

        composable<Route.AddMedication> {
            AddMedicationScreen(
                onBackClick = { navController.popBackStack() },
                onMedicationAdded = {
                    navController.navigate(Route.MedicationList) {
                        popUpTo(Route.Home) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Route.EditMedication> {
            EditMedicationScreen(
                onBackClick = { navController.popBackStack() },
                onMedicationUpdated = {
                    navController.navigate(Route.MedicationList) {
                        popUpTo(Route.Home) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Route.AddMedicationRecord> {
            AddMedicationRecordScreen()
        }
    }
}