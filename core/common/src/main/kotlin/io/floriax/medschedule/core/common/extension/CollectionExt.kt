package io.floriax.medschedule.core.common.extension

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/10
 */
inline fun <T> List<T>.mapAt(index: Int, transform: (T) -> T): List<T> =
    mapIndexed { i, item -> if (i == index) transform(item) else item }
