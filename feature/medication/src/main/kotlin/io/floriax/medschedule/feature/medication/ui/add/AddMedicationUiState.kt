package io.floriax.medschedule.feature.medication.ui.add

import io.floriax.medschedule.shared.ui.base.UiState

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/27
 */
data class AddMedicationUiState(
    val medicationName: String = "",
    val medicationNameError: Boolean = false,
    val stockString: String = "",
    val stockError: Boolean = false,
    val doseUnit: String = "",
    val doseUnitError: Boolean = false,
    val notes: String = ""
) : UiState