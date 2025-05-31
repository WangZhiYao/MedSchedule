package io.floriax.medschedule.ui.medication.edit

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/31
 */
data class EditMedicationViewState(
    val id: Long = 0,
    val name: String = "",
    val nameError: Boolean = false,
    val doseUnit: String = "",
    val remark: String = "",
    val error: Boolean = false
)