package io.floriax.medschedule.navigation.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.floriax.medschedule.ui.main.MainRoute
import io.floriax.medschedule.ui.main.rememberMainScreenState
import kotlinx.serialization.Serializable

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/26
 */
@Serializable
data object MainRoute

fun NavController.navigateToMain(navOptions: NavOptions) =
    navigate(route = MainRoute, navOptions = navOptions)

fun NavGraphBuilder.mainScreen(
    onCreateMedicationPlanClick: () -> Unit,
    onAddMedicationClick: () -> Unit,
    onMedicationClick: (Long) -> Unit,
    onCreateMedicationLogClick: () -> Unit,
    onMedicationLogClick: (Long) -> Unit,
) {
    composable<MainRoute> {
        val mainScreenState = rememberMainScreenState()
        MainRoute(
            mainScreenState = mainScreenState,
            onCreateMedicationPlanClick = onCreateMedicationPlanClick,
            onAddMedicationClick = onAddMedicationClick,
            onMedicationClick = onMedicationClick,
            onCreateMedicationLogClick = onCreateMedicationLogClick,
            onMedicationLogClick = onMedicationLogClick
        )
    }
}