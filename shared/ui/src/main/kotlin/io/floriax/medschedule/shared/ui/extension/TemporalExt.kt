package io.floriax.medschedule.shared.ui.extension

import android.content.Context
import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import io.floriax.medschedule.shared.ui.R
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
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
    return formatLocalDateTime(context)
}

fun Instant.formatLocalDateTime(context: Context): String {
    val zoneId = ZoneId.systemDefault()
    val targetDateTime = this.atZone(zoneId).toLocalDateTime()
    val targetDate = targetDateTime.toLocalDate()
    val today = LocalDate.now(zoneId)

    val formattedTime = targetDateTime.toLocalTime().formatLocalized(context)

    val formattedDate = when {
        targetDate == today -> null
        targetDate == today.minusDays(1) -> context.getString(R.string.shared_ui_date_yesterday)
        targetDate.year == today.year ->
            targetDate.format(DateTimeFormatter.ofPattern(context.getString(R.string.shared_ui_date_pattern_short)))

        else ->
            targetDate.format(DateTimeFormatter.ofPattern(context.getString(R.string.shared_ui_date_pattern_long)))
    }

    return formattedDate?.let { "$it $formattedTime" } ?: formattedTime
}

fun LocalDate.toStartOfDayAtUtc(): Instant =
    this.atStartOfDay(ZoneOffset.UTC).toInstant()

@Composable
fun LocalDate.formatLocalized(): String {
    val context = LocalContext.current
    return this.formatLocalized(context)
}

fun LocalDate.formatLocalized(context: Context): String {
    val formatter =
        DateTimeFormatter.ofPattern(context.getString(R.string.shared_ui_date_pattern_long))
    return this.format(formatter)
}

@Composable
fun LocalTime.formatLocalized(): String {
    val context = LocalContext.current
    return this.formatLocalized(context)
}

fun LocalTime.formatLocalized(context: Context): String {
    val is24Hour = DateFormat.is24HourFormat(context)
    val patternRes = if (is24Hour) R.string.shared_ui_time_24 else R.string.shared_ui_time_12
    val formatter = DateTimeFormatter.ofPattern(context.getString(patternRes))
    return this.format(formatter)
}