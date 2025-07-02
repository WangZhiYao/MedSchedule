package io.floriax.medschedule.feature.medicationrecord.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.floriax.medschedule.feature.medicationrecord.ui.MedicationRecordRoute
import io.floriax.medschedule.feature.medicationrecord.ui.create.CreateMedicationRecordRoute
import kotlinx.serialization.Serializable

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/26
 */
@Serializable
data object MedicationRecordRoute

@Serializable
data object CreateMedicationRecordRoute

fun NavController.navigateToMedicationRecord(navOptions: NavOptions) =
    navigate(route = MedicationRecordRoute, navOptions)

fun NavController.navigateToCreateMedicationRecord() =
    navigate(route = CreateMedicationRecordRoute)

fun NavGraphBuilder.medicationRecordScreen(
    onCreateMedicationRecordClick: () -> Unit
) {
    composable<MedicationRecordRoute> {
        MedicationRecordRoute(
            onCreateMedicationRecordClick = onCreateMedicationRecordClick
        )
    }
}

fun NavGraphBuilder.createMedicationRecordScreen(
    onBackClick: () -> Unit
) {
    composable<CreateMedicationRecordRoute> {
        CreateMedicationRecordRoute(
            onBackClick = onBackClick
        )
    }
}