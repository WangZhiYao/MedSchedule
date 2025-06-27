package io.floriax.medschedule.shared.designsystem.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import io.floriax.medschedule.shared.designsystem.R

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/26
 */
object AppIcons {

    val Home = Icons.Rounded.Home
    val HomeBorder = Icons.Outlined.Home

    val CalendarClock
        @Composable
        get() = ImageVector.vectorResource(R.drawable.ic_calendar_clock_filled_24dp)
    val CalendarClockBorder
        @Composable
        get() = ImageVector.vectorResource(R.drawable.ic_calendar_clock_24dp)

    val MedicalServices = Icons.Rounded.MedicalServices
    val MedicalServicesBorder = Icons.Outlined.MedicalServices

    val History = Icons.Rounded.History
    val HistoryBorder = Icons.Outlined.History

    val Add = Icons.Rounded.Add

}