package io.floriax.medschedule

import androidx.compose.runtime.Composable
import io.floriax.medschedule.navigation.MedScheduleAppState
import io.floriax.medschedule.navigation.MedScheduleNavHost
import io.floriax.medschedule.navigation.rememberMedScheduleAppState

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
@Composable
fun MedScheduleApp(
    appState: MedScheduleAppState = rememberMedScheduleAppState(),
) {

    MedScheduleNavHost(appState = appState)

}