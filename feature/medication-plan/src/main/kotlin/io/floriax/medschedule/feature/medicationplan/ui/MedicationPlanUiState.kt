package io.floriax.medschedule.feature.medicationplan.ui

import io.floriax.medschedule.core.domain.model.MedicationPlan
import io.floriax.medschedule.shared.ui.base.UiState

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/10/6
 */
data class MedicationPlanUiState(
    val activePlans: List<MedicationPlan> = emptyList(),
    val inactivePlans: List<MedicationPlan> = emptyList()
) : UiState
