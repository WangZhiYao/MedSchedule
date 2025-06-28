package io.floriax.medschedule.feature.medication.ui.edit

import io.floriax.medschedule.core.domain.model.Medication

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/28
 */
data class EditMedicationViewState(
    val originalMedication: Medication? = null,
    val medicationName: String = "",
    val medicationNameError: Boolean = false,
    val stockString: String = "",
    val stockError: Boolean = false,
    val doseUnit: String = "",
    val doseUnitError: Boolean = false,
    val notes: String = ""
) {

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