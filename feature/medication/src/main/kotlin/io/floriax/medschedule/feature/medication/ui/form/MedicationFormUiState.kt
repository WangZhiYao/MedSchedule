package io.floriax.medschedule.feature.medication.ui.form

import io.floriax.medschedule.shared.ui.base.UiState

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/10/8
 */
interface MedicationFormUiState : UiState {
    val medicationName: String
    val medicationNameError: Boolean
    val stockString: String
    val stockError: Boolean
    val doseUnit: String
    val doseUnitError: Boolean
    val notes: String
}