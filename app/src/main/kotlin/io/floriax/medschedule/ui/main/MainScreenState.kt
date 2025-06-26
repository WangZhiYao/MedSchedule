package io.floriax.medschedule.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.util.trace
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import io.floriax.medschedule.feature.home.navigation.navigateToHome
import io.floriax.medschedule.feature.medication.navigation.navigateToMedicineCabinet
import io.floriax.medschedule.feature.record.navigation.navigateToMedicationRecord
import io.floriax.medschedule.feature.schedule.navigation.navigateToMedicationSchedule
import io.floriax.medschedule.navigation.main.MainDestination

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/26
 */
@Composable
fun rememberMainScreenState(
    navController: NavHostController = rememberNavController()
) = remember(navController) {
    MainScreenState(navController)
}

@Stable
data class MainScreenState(
    val navController: NavHostController
) {

    private val previousDestination = mutableStateOf<NavDestination?>(null)

    val currentDestination: NavDestination?
        @Composable get() {
            // Collect the currentBackStackEntryFlow as a state
            val currentEntry = navController.currentBackStackEntryFlow
                .collectAsState(initial = null)

            // Fallback to previousDestination if currentEntry is null
            return currentEntry.value?.destination.also { destination ->
                if (destination != null) {
                    previousDestination.value = destination
                }
            } ?: previousDestination.value
        }

    val currentMainDestination: MainDestination?
        @Composable get() {
            return MainDestination.entries.firstOrNull { mainDestination ->
                currentDestination?.hasRoute(route = mainDestination.route) == true
            }
        }

    fun navigateToMainDestination(destination: MainDestination) {
        trace("Navigation: ${destination.name}") {
            val navOptions = navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }

            when (destination) {
                MainDestination.HOME -> navController.navigateToHome(navOptions)
                MainDestination.MEDICATION_SCHEDULE -> navController.navigateToMedicationSchedule(
                    navOptions
                )

                MainDestination.MEDICINE_CABINET -> navController.navigateToMedicineCabinet(
                    navOptions
                )

                MainDestination.MEDICATION_RECORD -> navController.navigateToMedicationRecord(
                    navOptions
                )
            }
        }
    }
}