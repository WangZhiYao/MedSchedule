package io.floriax.meds.common.logging

import android.util.Log

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/18
 */
enum class LogLevel(val level: Int) {

    VERBOSE(Log.VERBOSE),

    DEBUG(Log.DEBUG),

    INFO(Log.INFO),

    WARN(Log.WARN),

    ERROR(Log.ERROR),

    ASSERT(Log.ASSERT),

    NONE(Int.MAX_VALUE)

}