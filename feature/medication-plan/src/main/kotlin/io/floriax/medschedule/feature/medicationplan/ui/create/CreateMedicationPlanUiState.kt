package io.floriax.medschedule.feature.medicationplan.ui.create

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/25
 */
data class CreateMedicationPlanUiState(
    val currentStep: CreateMedicationPlanStep = CreateMedicationPlanStep.BASIC_INFO,
    val name: String = "",
    val nameError: NameError? = null,
    val notes: String = "",
    val showPreviousStep: Boolean = false,
    val showSaveButton: Boolean = false,
)

enum class CreateMedicationPlanStep {
    BASIC_INFO,
    SCHEDULE_TYPE,
    DOSAGE,
    SAVE
}

fun CreateMedicationPlanStep.previous(): CreateMedicationPlanStep? {
    val ordinal = this.ordinal - 1
    return if (ordinal < 0) {
        null
    } else {
        CreateMedicationPlanStep.entries[ordinal]
    }
}

fun CreateMedicationPlanStep.next(): CreateMedicationPlanStep? {
    val ordinal = this.ordinal + 1
    return if (ordinal >= CreateMedicationPlanStep.entries.size) {
        null
    } else {
        CreateMedicationPlanStep.entries[ordinal]
    }
}

sealed class NameError {
    data object Empty : NameError()
}

fun CreateMedicationPlanStep.canShowPrevious(): Boolean = this.ordinal > 0

fun CreateMedicationPlanStep.canShowSave(): Boolean =
    this == CreateMedicationPlanStep.entries.last()
