package io.floriax.medschedule.common.ext

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/31
 */
private val FORMATTER_YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd")
private val FORMATTER_HH_MM = DateTimeFormatter.ofPattern("HH:mm")

fun LocalDate.formatDate(): String =
    FORMATTER_YYYY_MM_DD.format(this)

fun LocalDate.toUtcStartOfDayMillis(): Long =
    this.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

fun LocalTime.formatTime(): String =
    FORMATTER_HH_MM.format(this)
