package io.floriax.medschedule.data.repository

import io.floriax.medschedule.data.database.dao.MedicationDao
import io.floriax.medschedule.data.database.mapper.toModel
import io.floriax.medschedule.domain.model.Medication
import io.floriax.medschedule.domain.repository.MedicationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
class MedicationRepositoryImpl @Inject constructor(
    private val medicationDao: MedicationDao
) : MedicationRepository {

    override fun observeAll(): Flow<List<Medication>> =
        medicationDao.observeAll()
            .map { entityList ->
                entityList.map { entity ->
                    entity.toModel()
                }
            }
}