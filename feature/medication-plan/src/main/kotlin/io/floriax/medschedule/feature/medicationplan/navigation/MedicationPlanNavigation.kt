package io.floriax.medschedule.feature.medicationplan.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.floriax.medschedule.feature.medicationplan.ui.MedicationPlanRoute
import io.floriax.medschedule.feature.medicationplan.ui.create.CreateMedicationPlanRoute
import kotlinx.serialization.Serializable

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/26
 */
@Serializable
data object MedicationPlanRoute

@Serializable
data object CreateMedicationPlanRoute

fun NavController.navigateToMedicationPlan(navOptions: NavOptions) {
    navigate(route = MedicationPlanRoute, navOptions)
}

fun NavController.navigateToCreateMedicationPlan() {
    navigate(route = CreateMedicationPlanRoute)
}

fun NavGraphBuilder.medicationPlanScreen(
    onCreateMedicationPlanClick: () -> Unit
) {
    composable<MedicationPlanRoute> {
        MedicationPlanRoute(
            onCreateMedicationPlanClick = onCreateMedicationPlanClick
        )
    }
}

fun NavGraphBuilder.createMedicationPlanScreen(
    onBackClick: () -> Unit
) {
    composable<CreateMedicationPlanRoute> {
        CreateMedicationPlanRoute(
            onBackClick = onBackClick
        )
    }
}