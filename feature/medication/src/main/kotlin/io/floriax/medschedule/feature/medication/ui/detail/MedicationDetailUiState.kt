package io.floriax.medschedule.feature.medication.ui.detail

import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.shared.ui.base.UiState

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/10
 */
data class MedicationDetailUiState(
    val loading: Boolean = true,
    val error: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val medication: Medication? = null,
    val showAddStockBottomSheet: Boolean = false,
) : UiState