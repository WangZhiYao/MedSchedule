package io.floriax.medschedule.feature.schedule.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.floriax.medschedule.feature.schedule.ui.MedicationScheduleRoute
import kotlinx.serialization.Serializable

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/26
 */
@Serializable
data object MedicationScheduleRoute

fun NavController.navigateToMedicationSchedule(navOptions: NavOptions) {
    navigate(route = MedicationScheduleRoute, navOptions)
}

fun NavGraphBuilder.medicationScheduleScreen() {
    composable<MedicationScheduleRoute> {
        MedicationScheduleRoute()
    }
}