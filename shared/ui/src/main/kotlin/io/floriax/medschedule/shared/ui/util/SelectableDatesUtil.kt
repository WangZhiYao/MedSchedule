package io.floriax.medschedule.shared.ui.util

import androidx.compose.material3.SelectableDates
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/3
 */
object UpToTodaySelectableDates : SelectableDates {

    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val today = LocalDate.now()

        val selectedDate = Instant.ofEpochMilli(utcTimeMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        return !selectedDate.isAfter(today)
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year <= LocalDate.now().year
    }
}