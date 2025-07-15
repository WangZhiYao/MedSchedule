package io.floriax.medschedule.feature.medicationrecord.ui.detail

import io.floriax.medschedule.core.domain.model.MedicationRecord
import io.floriax.medschedule.shared.ui.base.UiState

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/14
 */
data class MedicationRecordDetailUiState(
    val loading: Boolean = true,
    val error: Boolean = false,
    val medicationRecord: MedicationRecord? = null,
    val showDeleteDialog: Boolean = false
) : UiState