package io.floriax.medschedule.feature.medicationrecord.ui.detail

import io.floriax.medschedule.core.domain.model.MedicationRecord

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/14
 */
data class MedicationRecordDetailViewState(
    val loading: Boolean = true,
    val error: Boolean = false,
    val medicationRecord: MedicationRecord? = null,
    val showDeleteDialog: Boolean = false
)