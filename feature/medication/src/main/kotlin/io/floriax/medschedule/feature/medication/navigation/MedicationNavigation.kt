package io.floriax.medschedule.feature.medication.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.core.domain.model.MedicationRecord
import io.floriax.medschedule.feature.medication.ui.MedicineCabinetRoute
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
data object MedicineCabinetRoute

@Serializable
data object AddMedicationRoute

@Serializable
data class MedicationDetailRoute(val medicationId: Long)

@Serializable
data class EditMedicationRoute(val medicationId: Long)

fun NavController.navigateToMedicineCabinet(navOptions: NavOptions) =
    navigate(route = MedicineCabinetRoute, navOptions)

fun NavController.navigateToAddMedication() =
    navigate(route = AddMedicationRoute)

fun NavController.navigateToMedicationDetail(medication: Medication) =
    navigate(route = MedicationDetailRoute(medication.id))

fun NavController.navigateToEditMedication(medication: Medication) =
    navigate(route = EditMedicationRoute(medication.id))

fun NavGraphBuilder.medicineCabinetScreen(
    onAddMedicationClick: () -> Unit,
    onMedicationClick: (Medication) -> Unit,
) {
    composable<MedicineCabinetRoute> {
        MedicineCabinetRoute(
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
    onEditClick: (Medication) -> Unit,
    onMedicationRecordClick: (MedicationRecord) -> Unit,
) {
    composable<MedicationDetailRoute> {
        MedicationDetailRoute(
            onBackClick = onBackClick,
            onEditClick = onEditClick,
            onMedicationRecordClick = onMedicationRecordClick
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