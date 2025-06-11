package io.floriax.medschedule.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import io.floriax.medschedule.data.database.dao.MedicationRecordDao
import io.floriax.medschedule.data.database.mapper.toDetail
import io.floriax.medschedule.data.database.mapper.toEntity
import io.floriax.medschedule.data.database.relation.MedicationRecordWithTakenMedications
import io.floriax.medschedule.domain.model.MedicationRecord
import io.floriax.medschedule.domain.model.MedicationRecordDetail
import io.floriax.medschedule.domain.repository.MedicationRecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/1
 */
class MedicationRecordRepositoryImpl @Inject constructor(
    private val pagingConfig: PagingConfig,
    private val medicationRecordDao: MedicationRecordDao
) : MedicationRecordRepository {

    override suspend fun add(medicationRecord: MedicationRecord): MedicationRecord =
        medicationRecordDao.insert(medicationRecord.toEntity())
            .let { id ->
                medicationRecord.copy(id = id)
            }

    override fun observePagedMedicationRecordDetail(): Flow<PagingData<MedicationRecordDetail>> =
        Pager(pagingConfig) {
            medicationRecordDao.pagingWithTakenMedications()
        }
            .flow
            .map { pagingData ->
                pagingData.map(MedicationRecordWithTakenMedications::toDetail)
            }
}