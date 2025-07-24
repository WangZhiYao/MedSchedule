package io.floriax.medschedule.core.domain.repository

import androidx.paging.PagingData
import io.floriax.medschedule.core.domain.model.MedicationLog
import kotlinx.coroutines.flow.Flow

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/1
 */
interface MedicationLogRepository {

    fun observePaged(): Flow<PagingData<MedicationLog>>

    suspend fun add(medicationLog: MedicationLog): MedicationLog

    fun observeById(id: Long): Flow<MedicationLog?>

    fun observePagedByMedicationId(medicationId: Long): Flow<PagingData<MedicationLog>>

    suspend fun delete(medicationLog: MedicationLog, restoreMedicationStore: Boolean): Boolean

}