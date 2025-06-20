package io.floriax.medschedule.domain.repository

import io.floriax.medschedule.domain.model.Medication
import kotlinx.coroutines.flow.Flow

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
interface MedicationRepository {

    fun observeAll(): Flow<List<Medication>>

    suspend fun delete(medication: Medication): Boolean

    suspend fun add(medication: Medication): Medication

    suspend fun getById(id: Long): Medication?

    suspend fun update(medication: Medication): Medication

}