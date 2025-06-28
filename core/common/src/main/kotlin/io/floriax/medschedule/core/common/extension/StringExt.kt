package io.floriax.medschedule.core.common.extension

import java.math.BigDecimal

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/1
 */
fun String.isValidStock(): Boolean {
    if (isBlank()) {
        return false
    }
    val value = toBigDecimalOrNull() ?: return false
    return value > BigDecimal.ZERO
}

fun String.isValidDose(): Boolean {
    if (isBlank()) {
        return false
    }
    val value = toFloatOrNull() ?: return false
    return value > 0
}

fun String.nullIfBlank(): String? =
    takeIf { str -> str.isNotBlank() }
