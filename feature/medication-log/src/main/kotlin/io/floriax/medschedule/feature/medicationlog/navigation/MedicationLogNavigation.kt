package io.floriax.medschedule.feature.medicationlog.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.floriax.medschedule.feature.medicationlog.ui.MedicationLogRoute
import io.floriax.medschedule.feature.medicationlog.ui.create.CreateMedicationLogRoute
import io.floriax.medschedule.feature.medicationlog.ui.detail.MedicationLogDetailRoute
import kotlinx.serialization.Serializable

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/26
 */
@Serializable
data object MedicationLogRoute

@Serializable
data object CreateMedicationLogRoute

@Serializable
data class MedicationLogDetailRoute(val medicationLogId: Long)

fun NavController.navigateToMedicationLog(navOptions: NavOptions) =
    navigate(route = MedicationLogRoute, navOptions)

fun NavController.navigateToCreateMedicationLog() =
    navigate(route = CreateMedicationLogRoute)

fun NavController.navigateToMedicationLogDetail(medicationLogId: Long) =
    navigate(route = MedicationLogDetailRoute(medicationLogId))

fun NavGraphBuilder.medicationLogScreen(
    onCreateMedicationLogClick: () -> Unit,
    onMedicationLogClick: (Long) -> Unit
) {
    composable<MedicationLogRoute> {
        MedicationLogRoute(
            onCreateMedicationLogClick = onCreateMedicationLogClick,
            onMedicationLogClick = onMedicationLogClick
        )
    }
}

fun NavGraphBuilder.createMedicationLogScreen(
    onBackClick: () -> Unit,
    onAddMedicationClick: () -> Unit
) {
    composable<CreateMedicationLogRoute> {
        CreateMedicationLogRoute(
            onBackClick = onBackClick,
            onAddMedicationClick = onAddMedicationClick
        )
    }
}

fun NavGraphBuilder.medicationLogDetailScreen(
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
    composable<MedicationLogDetailRoute> {
        MedicationLogDetailRoute(
            onBackClick = onBackClick,
            onEditClick = onEditClick
        )
    }
}