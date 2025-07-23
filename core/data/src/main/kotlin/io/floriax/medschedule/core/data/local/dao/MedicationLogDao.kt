package io.floriax.medschedule.core.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import io.floriax.medschedule.core.data.local.entity.MedicationLogEntity
import io.floriax.medschedule.core.data.local.relation.MedicationLogWithEntries
import kotlinx.coroutines.flow.Flow

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/1
 */
@Dao
interface MedicationLogDao : BaseDao<MedicationLogEntity> {

    @Transaction
    @Query("SELECT * FROM medication_log ORDER BY medication_time DESC")
    fun observePaged(): PagingSource<Int, MedicationLogWithEntries>

    @Transaction
    @Query("SELECT * FROM medication_log WHERE id = :id")
    fun observeById(id: Long): Flow<MedicationLogWithEntries?>

    @Transaction
    @Query(
        """
        SELECT * FROM medication_log
        WHERE id IN (
            SELECT medication_log_id FROM medication_log_entry
            WHERE medication_id = :medicationId
        )
        ORDER BY medication_time DESC
    """
    )
    fun observePagedByMedicationId(medicationId: Long): PagingSource<Int, MedicationLogWithEntries>

}