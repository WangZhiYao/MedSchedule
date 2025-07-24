package io.floriax.medschedule.core.data.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import io.floriax.medschedule.core.common.exception.InsufficientStockException
import io.floriax.medschedule.core.data.extension.mapItems
import io.floriax.medschedule.core.data.extension.pager
import io.floriax.medschedule.core.data.local.AppDatabase
import io.floriax.medschedule.core.data.local.dao.MedicationDao
import io.floriax.medschedule.core.data.local.dao.MedicationLogDao
import io.floriax.medschedule.core.data.local.dao.MedicationLogEntryDao
import io.floriax.medschedule.core.data.local.entity.MedicationLogEntryEntity
import io.floriax.medschedule.core.data.local.mapper.toEntity
import io.floriax.medschedule.core.data.local.mapper.toModel
import io.floriax.medschedule.core.domain.model.MedicationLog
import io.floriax.medschedule.core.domain.repository.MedicationLogRepository
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
class MedicationLogRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val pagingConfig: PagingConfig,
    private val medicationDao: MedicationDao,
    private val medicationLogDao: MedicationLogDao,
    private val medicationLogEntryDao: MedicationLogEntryDao
) : MedicationLogRepository {

    override fun observePaged(): Flow<PagingData<MedicationLog>> =
        pager(pagingConfig) {
            medicationLogDao.observePaged()
        }
            .mapItems { entity ->
                entity.toModel()
            }

    override suspend fun add(medicationLog: MedicationLog): MedicationLog =
        appDatabase.withTransaction {
            val entity = medicationLog.toEntity()
            val id = medicationLogDao.insert(entity)

            val takenMedications = medicationLog.takenMedications
            val entries = takenMedications.map { takenMedication -> takenMedication.toEntity(id) }

            updateMedicationStocksIfNeeded(entries, false)

            medicationLogEntryDao.insertBatch(entries)

            medicationLog.copy(id = id)
        }

    override fun observeById(id: Long): Flow<MedicationLog?> =
        medicationLogDao.observeById(id)
            .map { entity ->
                entity?.toModel()
            }

    override fun observePagedByMedicationId(medicationId: Long): Flow<PagingData<MedicationLog>> =
        pager(pagingConfig) {
            medicationLogDao.observePagedByMedicationId(medicationId)
        }
            .mapItems { entity ->
                entity.toModel()
            }

    override suspend fun delete(
        medicationLog: MedicationLog,
        restoreMedicationStore: Boolean
    ): Boolean =
        appDatabase.withTransaction {
            if (restoreMedicationStore) {
                val takenMedications = medicationLog.takenMedications
                val entries = takenMedications.map { takenMedication ->
                    takenMedication.toEntity(medicationLog.id)
                }

                updateMedicationStocksIfNeeded(entries, true)
            }

            medicationLogDao.delete(medicationLog.toEntity()) > 0
        }

    private suspend fun updateMedicationStocksIfNeeded(
        entries: List<MedicationLogEntryEntity>,
        isRestoring: Boolean
    ) {
        val medicationIds = entries.filter { entry -> entry.deductFromStock }
            .map { entry -> entry.medicationId }
            .distinct()

        if (medicationIds.isEmpty()) {
            return
        }

        val medicationsToUpdate = medicationDao.getByIds(medicationIds)
            .filter { medication -> medication.stock?.toBigDecimalOrNull() != null }

        if (medicationsToUpdate.isEmpty()) {
            return
        }

        val entryMap = entries.associateBy { entry -> entry.medicationId }

        val updatedMedications = medicationsToUpdate.mapNotNull { medication ->
            val entry = entryMap[medication.id] ?: return@mapNotNull null

            val dose = entry.dose.toBigDecimal()
            val currentStock = medication.stock!!.toBigDecimal()
            val newStock = if (isRestoring) {
                currentStock + dose
            } else {
                val newStock = currentStock - dose
                if (newStock < BigDecimal.ZERO) {
                    throw InsufficientStockException(medication.name)
                }
                newStock
            }
            medication.copy(stock = newStock.toPlainString())
        }

        if (updatedMedications.isNotEmpty()) {
            medicationDao.updateBatch(updatedMedications)
        }
    }
}