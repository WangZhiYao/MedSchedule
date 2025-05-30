package io.floriax.medschedule.ui.medication

import io.floriax.medschedule.domain.model.Medication

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
data class MedicationListViewState(
    val medicationList: List<Medication> = emptyList()
)