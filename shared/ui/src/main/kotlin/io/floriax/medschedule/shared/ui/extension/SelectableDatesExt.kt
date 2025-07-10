package io.floriax.medschedule.shared.ui.extension

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import java.time.Instant
import java.time.LocalDate

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/3
 */
@OptIn(ExperimentalMaterial3Api::class)
object UpToTodaySelectableDates : SelectableDates {

    override fun isSelectableDate(utcTimeMillis: Long): Boolean =
        utcTimeMillis <= Instant.now().toEpochMilli()

    override fun isSelectableYear(year: Int): Boolean =
        year <= LocalDate.now().year

}