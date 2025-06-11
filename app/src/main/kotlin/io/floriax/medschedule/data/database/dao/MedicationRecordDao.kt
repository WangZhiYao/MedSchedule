package io.floriax.medschedule.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import io.floriax.medschedule.data.database.entity.MedicationRecordEntity
import io.floriax.medschedule.data.database.relation.MedicationRecordWithTakenMedications

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/1
 */
@Dao
interface MedicationRecordDao : IDao<MedicationRecordEntity> {

    @Transaction
    @Query("SELECT * FROM medication_record ORDER BY medication_time DESC")
    fun pagingWithTakenMedications(): PagingSource<Int, MedicationRecordWithTakenMedications>

}