package io.floriax.medschedule.common.ext

import android.icu.text.DecimalFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/31
 */
private val FORMATTER_DOSE = DecimalFormat("#.##")

fun Float.formatDose(): String =
    FORMATTER_DOSE.format(this)

fun Long.toLocalDateFromUtc(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()

