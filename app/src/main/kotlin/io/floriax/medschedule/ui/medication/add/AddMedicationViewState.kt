package io.floriax.medschedule.ui.medication.add

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/31
 */
data class AddMedicationViewState(
    val name: String = "",
    val nameError: Boolean = false,
    val doseUnit: String = "",
    val remark: String = ""
)