package io.floriax.medschedule.feature.medicationrecord.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.floriax.medschedule.feature.medicationrecord.ui.MedicationRecordRoute
import kotlinx.serialization.Serializable

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/26
 */
@Serializable
data object MedicationRecordRoute

fun NavController.navigateToMedicationRecord(navOptions: NavOptions) =
    navigate(route = MedicationRecordRoute, navOptions)

fun NavGraphBuilder.medicationRecordScreen() {
    composable<MedicationRecordRoute> {
        MedicationRecordRoute()
    }
}