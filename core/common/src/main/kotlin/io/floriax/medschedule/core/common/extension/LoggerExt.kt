package io.floriax.medschedule.core.common.extension

import io.floriax.medschedule.core.common.logging.Logger
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
inline fun <reified T> logger(
    tag: String = T::class.java.simpleName
): ReadOnlyProperty<Any?, Logger> =
    object : ReadOnlyProperty<Any?, Logger> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Logger =
            Logger.create(tag)
    }