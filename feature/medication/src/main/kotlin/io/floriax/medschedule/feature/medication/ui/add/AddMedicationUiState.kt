package io.floriax.medschedule.feature.medication.ui.add

import io.floriax.medschedule.feature.medication.ui.form.MedicationFormUiState

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/27
 */
data class AddMedicationUiState(
    override val medicationName: String = "",
    override val medicationNameError: Boolean = false,
    override val stockString: String = "",
    override val stockError: Boolean = false,
    override val doseUnit: String = "",
    override val doseUnitError: Boolean = false,
    override val notes: String = ""
) : MedicationFormUiState
