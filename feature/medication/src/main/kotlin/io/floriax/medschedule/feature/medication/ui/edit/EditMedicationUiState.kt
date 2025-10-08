package io.floriax.medschedule.feature.medication.ui.edit

import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.feature.medication.ui.form.MedicationFormUiState

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/28
 */
data class EditMedicationUiState(
    val originalMedication: Medication? = null,
    override val medicationName: String = "",
    override val medicationNameError: Boolean = false,
    override val stockString: String = "",
    override val stockError: Boolean = false,
    override val doseUnit: String = "",
    override val doseUnitError: Boolean = false,
    override val notes: String = ""
) : MedicationFormUiState {

    fun hasContentChanged(): Boolean {
        if (originalMedication == null) {
            return false
        }
        return medicationName != originalMedication.name ||
                stockString.toBigDecimalOrNull() != originalMedication.stock ||
                doseUnit != originalMedication.doseUnit ||
                notes != originalMedication.notes
    }
}
