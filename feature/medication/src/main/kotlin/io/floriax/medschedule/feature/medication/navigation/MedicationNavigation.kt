package io.floriax.medschedule.feature.medication.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.floriax.medschedule.feature.medication.ui.MedicineCabinetRoute
import kotlinx.serialization.Serializable

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/26
 */
@Serializable
data object MedicineCabinetRoute

fun NavController.navigateToMedicineCabinet(navOptions: NavOptions) =
    navigate(route = MedicineCabinetRoute, navOptions)

fun NavGraphBuilder.medicineCabinetScreen() {
    composable<MedicineCabinetRoute> {
        MedicineCabinetRoute()
    }
}