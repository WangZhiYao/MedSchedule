package io.floriax.medschedule.feature.medicationplan.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.floriax.medschedule.feature.medicationplan.ui.MedicationPlanRoute
import kotlinx.serialization.Serializable

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/26
 */
@Serializable
data object MedicationPlanRoute

fun NavController.navigateToMedicationPlan(navOptions: NavOptions) {
    navigate(route = MedicationPlanRoute, navOptions)
}

fun NavGraphBuilder.medicationPlanScreen() {
    composable<MedicationPlanRoute> {
        MedicationPlanRoute()
    }
}