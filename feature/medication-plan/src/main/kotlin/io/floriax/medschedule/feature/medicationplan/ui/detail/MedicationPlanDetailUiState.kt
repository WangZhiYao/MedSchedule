package io.floriax.medschedule.feature.medicationplan.ui.detail

import io.floriax.medschedule.core.domain.model.MedicationPlan
import io.floriax.medschedule.shared.ui.base.UiState

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/10/8
 */
data class MedicationPlanDetailUiState(
    val loading: Boolean = true,
    val error: Boolean = false,
    val medicationPlan: MedicationPlan? = null,
    val showDeleteDialog: Boolean = false,
) : UiState
