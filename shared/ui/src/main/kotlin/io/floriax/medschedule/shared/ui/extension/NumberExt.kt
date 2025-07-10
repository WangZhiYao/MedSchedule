package io.floriax.medschedule.shared.ui.extension

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/31
 */
fun Long.toLocalDateAtZone(zoneId: ZoneId = ZoneId.systemDefault()): LocalDate =
    Instant.ofEpochMilli(this).atZone(zoneId).toLocalDate()
