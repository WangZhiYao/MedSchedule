package io.floriax.medschedule.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import io.floriax.medschedule.navigation.main.MainRoute
import io.floriax.medschedule.navigation.main.mainScreen
import io.floriax.medschedule.navigation.main.rememberMainScreenState

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

    val mainScreenState = rememberMainScreenState()

    NavHost(
        navController = navController,
        startDestination = MainRoute,
        modifier = modifier,
    ) {
        mainScreen(mainScreenState)
    }
}