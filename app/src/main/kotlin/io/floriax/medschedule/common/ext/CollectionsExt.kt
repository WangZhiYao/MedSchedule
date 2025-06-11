package io.floriax.medschedule.common.ext

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/8
 */
fun <T> Iterable<T>.replaceAt(index: Int, element: T): List<T> =
    mapIndexed { i, old ->
        if (i == index) element else old
    }

fun <T> Iterable<T>.removeAt(index: Int): List<T> =
    filterIndexed { i, _ -> i != index }
