package io.floriax.medschedule.feature.medication.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.floriax.medschedule.feature.medication.ui.MedicationsRoute
import io.floriax.medschedule.feature.medication.ui.add.AddMedicationRoute
import io.floriax.medschedule.feature.medication.ui.detail.MedicationDetailRoute
import io.floriax.medschedule.feature.medication.ui.edit.EditMedicationRoute
import kotlinx.serialization.Serializable

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/26
 */
@Serializable
data object MedicationsRoute

@Serializable
data object AddMedicationRoute

@Serializable
data class MedicationDetailRoute(val medicationId: Long)

@Serializable
data class EditMedicationRoute(val medicationId: Long)

fun NavController.navigateToMedications(navOptions: NavOptions) =
    navigate(route = MedicationsRoute, navOptions)

fun NavController.navigateToAddMedication() =
    navigate(route = AddMedicationRoute)

fun NavController.navigateToMedicationDetail(medicationId: Long) =
    navigate(route = MedicationDetailRoute(medicationId))

fun NavController.navigateToEditMedication(medicationId: Long) =
    navigate(route = EditMedicationRoute(medicationId))

fun NavGraphBuilder.medicationsScreen(
    onAddMedicationClick: () -> Unit,
    onMedicationClick: (Long) -> Unit,
) {
    composable<MedicationsRoute> {
        MedicationsRoute(
            onAddMedicationClick = onAddMedicationClick,
            onMedicationClick = onMedicationClick
        )
    }
}

fun NavGraphBuilder.addMedicationScreen(
    onBackClick: () -> Unit,
) {
    composable<AddMedicationRoute> {
        AddMedicationRoute(onBackClick = onBackClick)
    }
}

fun NavGraphBuilder.medicationDetailScreen(
    onBackClick: () -> Unit,
    onEditClick: (Long) -> Unit,
    onMedicationLogClick: (Long) -> Unit,
) {
    composable<MedicationDetailRoute> {
        MedicationDetailRoute(
            onBackClick = onBackClick,
            onEditClick = onEditClick,
            onMedicationLogClick = onMedicationLogClick
        )
    }
}

fun NavGraphBuilder.editMedicationScreen(
    onBackClick: () -> Unit,
) {
    composable<EditMedicationRoute> {
        EditMedicationRoute(onBackClick = onBackClick)
    }
}