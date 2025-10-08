package io.floriax.medschedule.feature.medicationplan.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.floriax.medschedule.feature.medicationplan.ui.MedicationPlanRoute
import io.floriax.medschedule.feature.medicationplan.ui.create.CreateMedicationPlanRoute
import io.floriax.medschedule.feature.medicationplan.ui.detail.MedicationPlanDetailRoute
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

@Serializable
data class MedicationPlanDetailRoute(val medicationPlanId: Long)

fun NavController.navigateToMedicationPlan(navOptions: NavOptions) {
    navigate(route = MedicationPlanRoute, navOptions)
}

fun NavController.navigateToCreateMedicationPlan() {
    navigate(route = CreateMedicationPlanRoute)
}

fun NavController.navigateToMedicationPlanDetail(planId: Long) {
    navigate(MedicationPlanDetailRoute(planId))
}

fun NavGraphBuilder.medicationPlanScreen(
    onCreateMedicationPlanClick: () -> Unit,
    onMedicationPlanClick: (Long) -> Unit,
) {
    composable<MedicationPlanRoute> {
        MedicationPlanRoute(
            onCreateMedicationPlanClick = onCreateMedicationPlanClick,
            onMedicationPlanClick = onMedicationPlanClick
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

fun NavGraphBuilder.medicationPlanDetailScreen(
    onBackClick: () -> Unit,
    onEditClick: (Long) -> Unit,
) {
    composable<MedicationPlanDetailRoute> {
        MedicationPlanDetailRoute(
            onBackClick = onBackClick,
            onEditClick = onEditClick
        )
    }
}