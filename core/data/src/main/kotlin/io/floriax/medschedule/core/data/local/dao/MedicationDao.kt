package io.floriax.medschedule.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import io.floriax.medschedule.data.database.entity.MedicationEntity
import kotlinx.coroutines.flow.Flow

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
@Dao
interface MedicationDao : IDao<MedicationEntity> {

    @Query("SELECT * FROM medication")
    fun observeAll(): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medication WHERE id = :id")
    suspend fun getById(id: Long): MedicationEntity?

}