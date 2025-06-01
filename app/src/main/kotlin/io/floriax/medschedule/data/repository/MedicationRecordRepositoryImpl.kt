package io.floriax.medschedule.data.repository

import io.floriax.medschedule.data.database.dao.MedicationRecordDao
import io.floriax.medschedule.data.database.mapper.toEntity
import io.floriax.medschedule.domain.model.MedicationRecord
import io.floriax.medschedule.domain.repository.MedicationRecordRepository
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/1
 */
class MedicationRecordRepositoryImpl @Inject constructor(
    private val medicationRecordDao: MedicationRecordDao
) : MedicationRecordRepository {

    override suspend fun add(medicationRecord: MedicationRecord): MedicationRecord =
        medicationRecordDao.insert(medicationRecord.toEntity())
            .let { id ->
                medicationRecord.copy(id = id)
            }

}