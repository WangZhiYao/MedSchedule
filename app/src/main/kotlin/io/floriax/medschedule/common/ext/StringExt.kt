package io.floriax.medschedule.common.ext

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/1
 */
private val REGEX_DOSE = Regex("^(0|[1-9]\\d*)(\\.\\d{1,2})?$")

fun String.isValidDose(): Boolean =
    matches(REGEX_DOSE) && toFloatOrNull() != null
