package io.floriax.medschedule.feature.medication.ui.detail

import io.floriax.medschedule.core.domain.model.Medication

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/10
 */
data class MedicationDetailViewState(
    val loading: Boolean = true,
    val error: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val medication: Medication? = null,
    val showAddStockBottomSheet: Boolean = false,
)