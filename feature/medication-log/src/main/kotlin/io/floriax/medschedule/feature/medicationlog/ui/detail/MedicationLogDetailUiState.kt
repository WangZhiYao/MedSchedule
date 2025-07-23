package io.floriax.medschedule.feature.medicationlog.ui.detail

import io.floriax.medschedule.core.domain.model.MedicationLog
import io.floriax.medschedule.shared.ui.base.UiState

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/14
 */
data class MedicationLogDetailUiState(
    val loading: Boolean = true,
    val error: Boolean = false,
    val medicationLog: MedicationLog? = null,
    val showDeleteDialog: Boolean = false
) : UiState