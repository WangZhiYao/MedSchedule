package io.floriax.medschedule.core.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import io.floriax.medschedule.core.data.local.entity.MedicationRecordEntity
import io.floriax.medschedule.core.data.local.entity.MedicationRecordEntryEntity
import io.floriax.medschedule.core.data.local.relation.MedicationRecordWithEntries

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/1
 */
@Dao
interface MedicationRecordDao : IDao<MedicationRecordEntity> {

    @Transaction
    @Query("SELECT * FROM medication_record ORDER BY medication_time DESC")
    fun observePaged(): PagingSource<Int, MedicationRecordWithEntries>

    @Insert
    suspend fun insertEntries(entries: List<MedicationRecordEntryEntity>)

    @Transaction
    suspend fun insertWithEntries(
        item: MedicationRecordEntity,
        entries: List<MedicationRecordEntryEntity>
    ): Long {
        val id = insert(item)
        insertEntries(entries.map { entry -> entry.copy(medicationRecordId = id) })
        return id
    }
}