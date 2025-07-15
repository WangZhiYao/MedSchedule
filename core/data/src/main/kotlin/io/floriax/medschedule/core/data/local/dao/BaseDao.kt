package io.floriax.medschedule.core.data.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
interface BaseDao<T> {

    @Insert
    suspend fun insert(item: T): Long

    @Delete
    suspend fun delete(item: T): Int

    @Update
    suspend fun update(item: T): Int

}