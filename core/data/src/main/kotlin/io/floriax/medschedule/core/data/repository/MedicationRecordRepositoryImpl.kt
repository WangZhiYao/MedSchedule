package io.floriax.medschedule.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.withTransaction
import io.floriax.medschedule.core.common.exception.InsufficientStockException
import io.floriax.medschedule.core.data.local.AppDatabase
import io.floriax.medschedule.core.data.local.dao.MedicationDao
import io.floriax.medschedule.core.data.local.dao.MedicationRecordDao
import io.floriax.medschedule.core.data.local.dao.MedicationRecordEntryDao
import io.floriax.medschedule.core.data.local.entity.MedicationRecordEntryEntity
import io.floriax.medschedule.core.data.local.mapper.toEntity
import io.floriax.medschedule.core.data.local.mapper.toModel
import io.floriax.medschedule.core.domain.model.MedicationRecord
import io.floriax.medschedule.core.domain.repository.MedicationRecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/1
 */
class MedicationRecordRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val pagingConfig: PagingConfig,
    private val medicationDao: MedicationDao,
    private val medicationRecordDao: MedicationRecordDao,
    private val medicationRecordEntryDao: MedicationRecordEntryDao
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

    override suspend fun add(medicationRecord: MedicationRecord): MedicationRecord =
        appDatabase.withTransaction {
            val entity = medicationRecord.toEntity()
            val id = medicationRecordDao.insert(entity)

            val takenMedications = medicationRecord.takenMedications
            val entries = takenMedications.map { takenMedication -> takenMedication.toEntity(id) }

            updateMedicationStocksIfNeeded(entries)

            medicationRecordEntryDao.insertBatch(entries)

            medicationRecord.copy(id = id)
        }

    private suspend fun updateMedicationStocksIfNeeded(entries: List<MedicationRecordEntryEntity>) {
        val medicationIds = entries.filter { entry -> entry.deductFromStock }
            .map { entry -> entry.medicationId }
            .distinct()

        if (medicationIds.isEmpty()) {
            return
        }

        val medicationsToUpdate = medicationDao.getByIds(medicationIds)
            .filter { medication -> !medication.stock.isNullOrBlank() }

        if (medicationsToUpdate.isEmpty()) {
            return
        }

        val entryMap = entries.associateBy { entry -> entry.medicationId }

        val updatedMedications = medicationsToUpdate.mapNotNull { medication ->
            val entry = entryMap[medication.id] ?: return@mapNotNull null

            val dose = entry.dose.toBigDecimal()
            val currentStock = medication.stock!!.toBigDecimal()
            val newStock = currentStock - dose

            if (newStock < BigDecimal.ZERO) {
                throw InsufficientStockException(medication.name)
            }

            medication.copy(stock = newStock.toPlainString())
        }

        if (updatedMedications.isNotEmpty()) {
            medicationDao.updateBatch(updatedMedications)
        }
    }

    override fun observeById(id: Long): Flow<MedicationRecord?> =
        medicationRecordDao.observeById(id)
            .map { entity ->
                entity?.toModel()
            }

    override fun observePagedByMedicationId(medicationId: Long): Flow<PagingData<MedicationRecord>> =
        Pager(pagingConfig) {
            medicationRecordDao.observePagedByMedicationId(medicationId)
        }
            .flow
            .map { entityList ->
                entityList.map { entity ->
                    entity.toModel()
                }
            }

    override suspend fun delete(medicationRecord: MedicationRecord): Boolean =
        medicationRecordDao.delete(medicationRecord.toEntity()) > 0
}