package io.floriax.medschedule.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.floriax.medschedule.ui.home.HomeRoute
import io.floriax.medschedule.ui.medication.MedicationListRoute
import io.floriax.medschedule.ui.medication.add.AddMedicationRoute
import io.floriax.medschedule.ui.medication.edit.EditMedicationRoute
import io.floriax.medschedule.ui.medication.record.add.AddMedicationRecordRoute

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
            HomeRoute(
                onMedicationClick = { navController.navigate(Route.MedicationList) },
                onAddMedicationRecordClick = { navController.navigate(Route.AddMedicationRecord) }
            )
        }

        composable<Route.MedicationList> {
            MedicationListRoute(
                onBackClick = { navController.popBackStack() },
                onAddMedicationClick = { navController.navigate(Route.AddMedication) },
                onEditMedicationClick = { medication ->
                    navController.navigate(Route.EditMedication(medication.id))
                },
            )
        }

        composable<Route.AddMedication> {
            AddMedicationRoute(
                onBackClick = { navController.popBackStack() },
                onMedicationAdded = { navController.popBackStack() }
            )
        }

        composable<Route.EditMedication> {
            EditMedicationRoute(
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
            AddMedicationRecordRoute(
                onBackClick = { navController.popBackStack() },
                onAddMedicationClick = { navController.navigate(Route.AddMedication) },
                onMedicationRecordAdded = {
                    navController.navigate(Route.Home) {
                        popUpTo(Route.Home) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}