package io.floriax.medschedule.feature.medicationrecord.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.floriax.medschedule.core.domain.model.MedicationRecord
import io.floriax.medschedule.feature.medicationrecord.ui.MedicationRecordRoute
import io.floriax.medschedule.feature.medicationrecord.ui.create.CreateMedicationRecordRoute
import io.floriax.medschedule.feature.medicationrecord.ui.detail.MedicationRecordDetailRoute
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

@Serializable
data class MedicationRecordDetailRoute(val medicationRecordId: Long)

fun NavController.navigateToMedicationRecord(navOptions: NavOptions) =
    navigate(route = MedicationRecordRoute, navOptions)

fun NavController.navigateToCreateMedicationRecord() =
    navigate(route = CreateMedicationRecordRoute)

fun NavController.navigateToMedicationRecordDetail(medicationRecordId: Long) =
    navigate(route = MedicationRecordDetailRoute(medicationRecordId))

fun NavGraphBuilder.medicationRecordScreen(
    onCreateMedicationRecordClick: () -> Unit,
    onMedicationRecordClick: (MedicationRecord) -> Unit
) {
    composable<MedicationRecordRoute> {
        MedicationRecordRoute(
            onCreateMedicationRecordClick = onCreateMedicationRecordClick,
            onMedicationRecordClick = onMedicationRecordClick
        )
    }
}

fun NavGraphBuilder.createMedicationRecordScreen(
    onBackClick: () -> Unit,
    onAddMedicationClick: () -> Unit
) {
    composable<CreateMedicationRecordRoute> {
        CreateMedicationRecordRoute(
            onBackClick = onBackClick,
            onAddMedicationClick = onAddMedicationClick
        )
    }
}

fun NavGraphBuilder.medicationRecordDetailScreen(
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
    composable<MedicationRecordDetailRoute> {
        MedicationRecordDetailRoute(
            onBackClick = onBackClick,
            onEditClick = onEditClick
        )
    }
}