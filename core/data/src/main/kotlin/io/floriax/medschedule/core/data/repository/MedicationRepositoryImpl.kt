package io.floriax.medschedule.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import io.floriax.medschedule.core.data.local.dao.MedicationDao
import io.floriax.medschedule.core.data.local.mapper.toEntity
import io.floriax.medschedule.core.data.local.mapper.toModel
import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.core.domain.repository.MedicationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/27
 */
class MedicationRepositoryImpl @Inject constructor(
    private val medicationDao: MedicationDao,
    private val pagingConfig: PagingConfig,
) : MedicationRepository {

    override fun observePaged(): Flow<PagingData<Medication>> =
        Pager(pagingConfig) {
            medicationDao.observePaged()
        }
            .flow
            .map { entityList ->
                entityList.map { entity ->
                    entity.toModel()
                }
            }

    override suspend fun delete(medication: Medication): Boolean =
        medicationDao.delete(medication.toEntity()) > 0

    override suspend fun add(medication: Medication): Medication =
        medicationDao.insert(medication.toEntity())
            .let { id ->
                medication.copy(id = id)
            }

    override suspend fun getById(id: Long): Medication? =
        medicationDao.getById(id)?.toModel()

    override fun observeById(id: Long): Flow<Medication?> =
        medicationDao.observeById(id)
            .map { entity ->
                entity?.toModel()
            }

    override suspend fun update(medication: Medication): Medication =
        medicationDao.update(medication.toEntity())
            .let {
                medication
            }
}