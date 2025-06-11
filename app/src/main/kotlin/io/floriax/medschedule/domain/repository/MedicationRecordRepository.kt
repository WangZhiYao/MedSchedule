package io.floriax.medschedule.domain.repository

import androidx.paging.PagingData
import io.floriax.medschedule.domain.model.MedicationRecord
import io.floriax.medschedule.domain.model.MedicationRecordDetail
import kotlinx.coroutines.flow.Flow

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/1
 */
interface MedicationRecordRepository {

    suspend fun add(medicationRecord: MedicationRecord): MedicationRecord

    fun observePagedMedicationRecordDetail(): Flow<PagingData<MedicationRecordDetail>>

}