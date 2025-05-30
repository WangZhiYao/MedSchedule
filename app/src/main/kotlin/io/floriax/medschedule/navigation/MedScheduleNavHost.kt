package io.floriax.medschedule.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.floriax.medschedule.ui.home.HomeScreen

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
                onMedicationClick = {},
                onAddMedicationRecordClick = {}
            )
        }

    }
}