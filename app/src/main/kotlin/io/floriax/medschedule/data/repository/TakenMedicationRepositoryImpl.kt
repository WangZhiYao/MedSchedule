package io.floriax.medschedule.data.repository

import io.floriax.medschedule.data.database.dao.TakenMedicationDao
import io.floriax.medschedule.data.database.mapper.toEntity
import io.floriax.medschedule.domain.model.TakenMedication
import io.floriax.medschedule.domain.repository.TakenMedicationRepository
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/4
 */
class TakenMedicationRepositoryImpl @Inject constructor(
    private val takenMedicationDao: TakenMedicationDao
) : TakenMedicationRepository {

    override suspend fun add(takenMedication: TakenMedication): TakenMedication =
        takenMedicationDao.insert(takenMedication.toEntity())
            .let { id ->
                takenMedication.copy(id = id)
            }

    override suspend fun addBatch(takenMedications: List<TakenMedication>): List<TakenMedication> {
        val ids = takenMedicationDao.insertBatch(
            takenMedications.map { takenMedication ->
                takenMedication.toEntity()
            }
        )
        return takenMedications.zip(ids) { takenMedication, id ->
            takenMedication.copy(id = id)
        }
    }
}