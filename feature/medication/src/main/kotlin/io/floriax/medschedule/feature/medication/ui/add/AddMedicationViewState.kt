package io.floriax.medschedule.feature.medication.ui.add

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/27
 */
data class AddMedicationViewState(
    val medicationName: String = "",
    val medicationNameError: Boolean = false,
    val stockString: String = "",
    val stockError: Boolean = false,
    val doseUnit: String = "",
    val doseUnitError: Boolean = false,
    val notes: String = ""
)