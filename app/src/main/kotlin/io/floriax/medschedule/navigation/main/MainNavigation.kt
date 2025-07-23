package io.floriax.medschedule.navigation.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.core.domain.model.MedicationLog
import io.floriax.medschedule.ui.main.MainRoute
import io.floriax.medschedule.ui.main.rememberMainScreenState
import kotlinx.serialization.Serializable

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/26
 */
@Serializable
data object MainRoute

fun NavController.navigateToMain(navOptions: NavOptions) =
    navigate(route = MainRoute, navOptions = navOptions)

fun NavGraphBuilder.mainScreen(
    onAddMedicationClick: () -> Unit,
    onMedicationClick: (Medication) -> Unit,
    onCreateMedicationLogClick: () -> Unit,
    onMedicationLogClick: (MedicationLog) -> Unit,
) {
    composable<MainRoute> {
        val mainScreenState = rememberMainScreenState()
        MainRoute(
            mainScreenState = mainScreenState,
            onAddMedicationClick = onAddMedicationClick,
            onMedicationClick = onMedicationClick,
            onCreateMedicationLogClick = onCreateMedicationLogClick,
            onMedicationLogClick = onMedicationLogClick
        )
    }
}