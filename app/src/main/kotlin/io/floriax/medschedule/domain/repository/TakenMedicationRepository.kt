package io.floriax.medschedule.domain.repository

import io.floriax.medschedule.domain.model.TakenMedication

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/4
 */
interface TakenMedicationRepository {

    suspend fun add(takenMedication: TakenMedication): TakenMedication

    suspend fun addBatch(takenMedications: List<TakenMedication>): List<TakenMedication>

}