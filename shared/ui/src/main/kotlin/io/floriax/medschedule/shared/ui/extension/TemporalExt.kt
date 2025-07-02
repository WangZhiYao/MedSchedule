package io.floriax.medschedule.shared.ui.extension

import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import io.floriax.medschedule.shared.ui.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/1
 */
@Composable
fun Instant.formatLocalDateTime(): String {
    val context = LocalContext.current
    val zoneId = ZoneId.systemDefault()
    val targetDateTime = this.atZone(zoneId).toLocalDateTime()
    val targetDate = targetDateTime.toLocalDate()
    val today = LocalDate.now(zoneId)

    val is24HourFormat = DateFormat.is24HourFormat(context)
    val timePatternRes =
        if (is24HourFormat) R.string.shared_ui_time_24 else R.string.shared_ui_time_12
    val timeFormatter = DateTimeFormatter.ofPattern(context.getString(timePatternRes))
    val formattedTime = targetDateTime.format(timeFormatter)

    val formattedDate = when {
        targetDate == today -> null
        targetDate == today.minusDays(1) -> context.getString(R.string.shared_ui_date_yesterday)
        targetDate.year == today.year -> targetDate.format(
            DateTimeFormatter.ofPattern(
                context.getString(
                    R.string.shared_ui_date_pattern_short
                )
            )
        )

        else -> targetDate.format(DateTimeFormatter.ofPattern(context.getString(R.string.shared_ui_date_pattern_long)))
    }

    return if (formattedDate != null) {
        "$formattedDate $formattedTime"
    } else {
        formattedTime
    }
}