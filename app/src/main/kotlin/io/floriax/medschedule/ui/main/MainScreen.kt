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
import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.core.domain.model.MedicationRecord
import io.floriax.medschedule.ext.isRouteInHierarchy
import io.floriax.medschedule.feature.home.navigation.HomeRoute
import io.floriax.medschedule.feature.home.navigation.homeScreen
import io.floriax.medschedule.feature.medication.navigation.medicineCabinetScreen
import io.floriax.medschedule.feature.medicationplan.navigation.medicationPlanScreen
import io.floriax.medschedule.feature.medicationrecord.navigation.medicationRecordScreen
import io.floriax.medschedule.navigation.main.MainDestination
import io.floriax.medschedule.shared.designsystem.theme.AppTheme

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/26
 */
@Composable
fun MainRoute(
    mainScreenState: MainScreenState,
    onAddMedicationClick: () -> Unit,
    onMedicationClick: (Medication) -> Unit,
    onCreateMedicationRecordClick: () -> Unit,
    onMedicationRecordClick: (MedicationRecord) -> Unit,
) {

    MainScreen(
        mainScreenState = mainScreenState,
        onAddMedicationClick = onAddMedicationClick,
        onMedicationClick = onMedicationClick,
        onCreateMedicationRecordClick = onCreateMedicationRecordClick,
        onMedicationRecordClick = onMedicationRecordClick
    )
}

@Composable
private fun MainScreen(
    mainScreenState: MainScreenState,
    onAddMedicationClick: () -> Unit,
    onMedicationClick: (Medication) -> Unit,
    onCreateMedicationRecordClick: () -> Unit,
    onMedicationRecordClick: (MedicationRecord) -> Unit,
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
        MainNavHost(
            navController = mainScreenState.navController,
            onAddMedicationClick = onAddMedicationClick,
            onMedicationClick = onMedicationClick,
            onCreateMedicationRecordClick = onCreateMedicationRecordClick,
            onMedicationRecordClick = onMedicationRecordClick
        )
    }
}

@Composable
private fun MainNavHost(
    navController: NavHostController,
    onAddMedicationClick: () -> Unit,
    onMedicationClick: (Medication) -> Unit,
    onCreateMedicationRecordClick: () -> Unit,
    onMedicationRecordClick: (MedicationRecord) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier
    ) {
        homeScreen()

        medicationPlanScreen()

        medicineCabinetScreen(
            onAddMedicationClick = onAddMedicationClick,
            onMedicationClick = onMedicationClick
        )

        medicationRecordScreen(
            onCreateMedicationRecordClick = onCreateMedicationRecordClick,
            onMedicationRecordClick = onMedicationRecordClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    AppTheme {
        MainScreen(
            mainScreenState = rememberMainScreenState(),
            onAddMedicationClick = {},
            onMedicationClick = {},
            onCreateMedicationRecordClick = {},
            onMedicationRecordClick = {}
        )
    }
}
