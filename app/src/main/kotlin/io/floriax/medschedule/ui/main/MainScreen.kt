package io.floriax.medschedule.ui.main

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import io.floriax.medschedule.core.ui.theme.AppTheme
import io.floriax.medschedule.ext.isRouteInHierarchy
import io.floriax.medschedule.feature.home.navigation.HomeRoute
import io.floriax.medschedule.feature.home.navigation.homeScreen
import io.floriax.medschedule.feature.medication.navigation.medicineCabinetScreen
import io.floriax.medschedule.feature.record.navigation.medicationRecordScreen
import io.floriax.medschedule.feature.schedule.navigation.medicationScheduleScreen
import io.floriax.medschedule.navigation.main.MainDestination
import io.floriax.medschedule.navigation.main.MainScreenState
import io.floriax.medschedule.navigation.main.rememberMainScreenState

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/26
 */
@Composable
fun MainRoute(
    mainScreenState: MainScreenState
) {

    MainScreen(
        mainScreenState = mainScreenState
    )
}

@Composable
private fun MainScreen(
    mainScreenState: MainScreenState,
    modifier: Modifier = Modifier,
) {

    val currentDestination = mainScreenState.currentDestination

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            MainDestination.entries.forEach { destination ->
                val selected = currentDestination.isRouteInHierarchy(destination.route)
                item(
                    selected = selected,
                    onClick = { mainScreenState.navigateToMainDestination(destination) },
                    icon = {
                        Icon(
                            imageVector = if (selected) {
                                destination.selectedIcon
                            } else {
                                destination.unselectedIcon
                            },
                            contentDescription = stringResource(destination.titleRes)
                        )
                    },
                    label = {
                        Text(text = stringResource(destination.titleRes))
                    }
                )
            }
        },
        modifier = modifier
    ) {
        MainNavHost(mainScreenState.navController)
    }
}

@Composable
private fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier
    ) {
        homeScreen()
        medicationScheduleScreen()
        medicineCabinetScreen()
        medicationRecordScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    AppTheme {
        MainScreen(
            mainScreenState = rememberMainScreenState()
        )
    }
}
