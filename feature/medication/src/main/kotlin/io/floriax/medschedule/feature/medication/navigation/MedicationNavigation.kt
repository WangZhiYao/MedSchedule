package io.floriax.medschedule.feature.medication.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.feature.medication.ui.MedicineCabinetRoute
import io.floriax.medschedule.feature.medication.ui.add.AddMedicationRoute
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

fun NavController.navigateToMedicineCabinet(navOptions: NavOptions) =
    navigate(route = MedicineCabinetRoute, navOptions)

fun NavController.navigateToAddMedication() =
    navigate(route = AddMedicationRoute)

fun NavGraphBuilder.medicineCabinetScreen(
    onAddMedicationClick: () -> Unit,
    onEditMedicationClick: (Medication) -> Unit,
) {
    composable<MedicineCabinetRoute> {
        MedicineCabinetRoute(
            onAddMedicationClick = onAddMedicationClick,
            onEditMedicationClick = onEditMedicationClick
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