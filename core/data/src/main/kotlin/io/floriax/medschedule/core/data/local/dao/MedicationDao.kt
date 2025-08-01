package io.floriax.medschedule.core.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import io.floriax.medschedule.core.data.local.entity.MedicationEntity
import kotlinx.coroutines.flow.Flow

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
@Dao
interface MedicationDao : BaseDao<MedicationEntity> {

    @Query("SELECT * FROM medication WHERE active = 1 ORDER BY name ASC")
    fun observePaged(): PagingSource<Int, MedicationEntity>

    @Query("SELECT * FROM medication WHERE id = :id")
    suspend fun getById(id: Long): MedicationEntity?

    @Query("SELECT * FROM medication WHERE id IN (:ids)")
    suspend fun getByIds(ids: List<Long>): List<MedicationEntity>

    @Query("SELECT * FROM medication WHERE id = :id")
    fun observeById(id: Long): Flow<MedicationEntity?>

    override suspend fun delete(item: MedicationEntity): Int =
        update(item.copy(active = false))

    @Update
    suspend fun updateBatch(items: List<MedicationEntity>)

}