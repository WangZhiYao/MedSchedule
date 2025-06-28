package io.floriax.medschedule.core.domain.repository

import androidx.paging.PagingData
import io.floriax.medschedule.core.domain.model.Medication
import kotlinx.coroutines.flow.Flow

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
interface MedicationRepository {

    fun observePaged(): Flow<PagingData<Medication>>

    suspend fun delete(medication: Medication): Boolean

    suspend fun add(medication: Medication): Medication

    suspend fun getById(id: Long): Medication?

    suspend fun update(medication: Medication): Medication

}