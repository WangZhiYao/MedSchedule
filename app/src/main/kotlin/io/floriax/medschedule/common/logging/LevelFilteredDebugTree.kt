package io.floriax.meds.common.logging

import timber.log.Timber

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/18
 */
class LevelFilteredDebugTree(private val minLevel: LogLevel) : Timber.DebugTree() {

    override fun isLoggable(tag: String?, priority: Int): Boolean =
        priority >= minLevel.level

}