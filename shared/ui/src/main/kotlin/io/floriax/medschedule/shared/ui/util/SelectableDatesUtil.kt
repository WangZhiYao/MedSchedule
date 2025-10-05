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

object FromTodaySelectableDates : SelectableDates {

    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val today = LocalDate.now()

        val selectedDate = Instant.ofEpochMilli(utcTimeMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        return !selectedDate.isBefore(today)
    }

    override fun isSelectableYear(year: Int): Boolean =
        year >= LocalDate.now().year
}

class BetweenDatesSelectableDates(
    private val startDate: LocalDate,
    private val endDate: LocalDate? = null,
    private val inclusiveStart: Boolean = true,
    private val inclusiveEnd: Boolean = true,
) : SelectableDates {

    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val date = Instant.ofEpochMilli(utcTimeMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        val afterStart = if (inclusiveStart) {
            !date.isBefore(startDate)
        } else {
            date.isAfter(startDate)
        }

        val beforeEnd = endDate?.let {
            if (inclusiveEnd) {
                !date.isAfter(it)
            } else {
                date.isBefore(it)
            }
        } ?: true

        return afterStart && beforeEnd
    }

    override fun isSelectableYear(year: Int): Boolean {
        val endYear = endDate?.year ?: year
        return year in startDate.year..endYear
    }
}

class AfterSpecialDateSelectableDates(
    val specialDate: LocalDate,
    val inclusive: Boolean = true
) : SelectableDates {

    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val selectedDate = Instant.ofEpochMilli(utcTimeMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        if (inclusive) {
            return !selectedDate.isBefore(specialDate)
        }
        return selectedDate.isAfter(specialDate)
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year >= specialDate.year
    }
}
