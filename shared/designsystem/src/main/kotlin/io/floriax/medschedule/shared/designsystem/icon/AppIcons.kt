package io.floriax.medschedule.shared.designsystem.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.HelpOutline
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.RemoveCircle
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
    val ArrowBack = Icons.AutoMirrored.Rounded.ArrowBack
    val ArrowForward = Icons.AutoMirrored.Rounded.ArrowForward
    val MoreVert = Icons.Rounded.MoreVert
    val Edit = Icons.Rounded.Edit
    val Delete = Icons.Rounded.Delete
    val ArrowDropDown = Icons.Rounded.ArrowDropDown
    val ArrowDropUp = Icons.Rounded.ArrowDropUp

    val MedicationSlimBorder
        @Composable
        get() = ImageVector.vectorResource(R.drawable.ic_medication_w200_24dp)
    val HistorySlimBorder
        @Composable
        get() = ImageVector.vectorResource(R.drawable.ic_history_w200_24dp)

    val CheckCircle = Icons.Rounded.CheckCircle
    val RemoveCircle = Icons.Rounded.RemoveCircle
    val Error = Icons.Rounded.Error
    val ErrorOutline = Icons.Outlined.ErrorOutline
    val RadioButtonUnchecked = Icons.Rounded.RadioButtonUnchecked
    val HelpOutline = Icons.AutoMirrored.Rounded.HelpOutline

    val CalendarToday = Icons.Rounded.CalendarToday
    val AccessTime = Icons.Rounded.AccessTime

    val CircleBorder = Icons.Outlined.Circle

}