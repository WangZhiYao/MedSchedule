package io.floriax.medschedule.core.data.extension

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/24
 */
fun <K : Any, V : Any> pager(
    config: PagingConfig,
    pagingSourceFactory: () -> PagingSource<K, V>
): Flow<PagingData<V>> =
    Pager(config) { pagingSourceFactory() }.flow

inline fun <T : Any, R : Any> Flow<PagingData<T>>.mapItems(
    crossinline transform: (T) -> R
): Flow<PagingData<R>> =
    map { pagingData -> pagingData.map { transform(it) } }