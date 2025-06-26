package io.floriax.medschedule.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.floriax.medschedule.ui.main.MainRoute
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

fun NavGraphBuilder.mainScreen() {
    composable<MainRoute> {
        MainRoute()
    }
}