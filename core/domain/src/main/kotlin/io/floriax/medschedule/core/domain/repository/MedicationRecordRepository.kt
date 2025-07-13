package io.floriax.medschedule.core.domain.repository

import androidx.paging.PagingData
import io.floriax.medschedule.core.domain.model.MedicationRecord
import kotlinx.coroutines.flow.Flow

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/1
 */
interface MedicationRecordRepository {

    fun observePaged(): Flow<PagingData<MedicationRecord>>

    suspend fun add(medicationRecord: MedicationRecord): MedicationRecord

    fun observePagedByMedicationId(medicationId: Long): Flow<PagingData<MedicationRecord>>

}