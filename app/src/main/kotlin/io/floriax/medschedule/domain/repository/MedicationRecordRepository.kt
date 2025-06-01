package io.floriax.medschedule.domain.repository

import io.floriax.medschedule.domain.model.MedicationRecord

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/1
 */
interface MedicationRecordRepository {

    suspend fun add(medicationRecord: MedicationRecord): MedicationRecord

}