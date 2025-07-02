package io.floriax.medschedule.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import io.floriax.medschedule.core.data.local.dao.MedicationRecordDao
import io.floriax.medschedule.core.data.local.mapper.toEntity
import io.floriax.medschedule.core.data.local.mapper.toModel
import io.floriax.medschedule.core.domain.model.MedicationRecord
import io.floriax.medschedule.core.domain.repository.MedicationRecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/1
 */
class MedicationRecordRepositoryImpl @Inject constructor(
    private val pagingConfig: PagingConfig,
    private val medicationRecordDao: MedicationRecordDao
) : MedicationRecordRepository {

    override fun observePaged(): Flow<PagingData<MedicationRecord>> =
        Pager(pagingConfig) {
            medicationRecordDao.observePaged()
        }
            .flow
            .map { entityList ->
                entityList.map { entity ->
                    entity.toModel()
                }
            }

    override suspend fun add(medicationRecord: MedicationRecord): MedicationRecord {
        val (record, entries) = medicationRecord.toEntity()
        val id = medicationRecordDao.insertWithEntries(record, entries)
        return medicationRecord.copy(id = id)
    }

}