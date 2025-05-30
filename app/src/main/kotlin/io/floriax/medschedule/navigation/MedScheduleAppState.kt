package io.floriax.medschedule.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
@Composable
fun rememberMedScheduleAppState(
    navController: NavHostController = rememberNavController()
) = remember(navController) {
    MedScheduleAppState(navController)
}

@Stable
data class MedScheduleAppState(
    val navController: NavHostController
)