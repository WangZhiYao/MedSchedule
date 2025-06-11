package io.floriax.medschedule.common.ext

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/1
 */
fun String.isValidDose(): Boolean {
    if (isBlank()) {
        return false
    }
    val value = toFloatOrNull() ?: return false
    return value > 0
}

fun String.nullIfBlank(): String? =
    takeIf { str -> str.isNotBlank() }
