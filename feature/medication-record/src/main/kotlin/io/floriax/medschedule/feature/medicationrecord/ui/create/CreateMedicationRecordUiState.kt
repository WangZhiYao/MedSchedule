package io.floriax.medschedule.feature.medicationrecord.ui.create

import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.core.domain.model.TakenMedication
import io.floriax.medschedule.shared.ui.base.UiState
import java.time.LocalDate
import java.time.LocalTime

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/2
 */
data class CreateMedicationRecordUiState(
    val showSelectDateDialog: Boolean = false,
    val selectedDate: LocalDate = LocalDate.now(),
    val showSelectTimeDialog: Boolean = false,
    val selectedTime: LocalTime = LocalTime.now(),
    val showAddMedicationDialog: Boolean = false,
    val takenMedicationInputs: List<TakenMedicationInput> = emptyList(),
    val showSelectMedicationBottomSheet: Boolean = false,
    val notes: String = ""
) : UiState

data class TakenMedicationInput(
    val medication: Medication,
    val doseString: String,
    val isMarkedAsError: Boolean = false
) {

    val isDoseEmpty: Boolean
        get() = doseString.toBigDecimalOrNull() == null

    val isStockExceeded: Boolean
        get() = medication.stock != null && doseString.isNotBlank()
                && doseString.toBigDecimal() > medication.stock

    val isDoseInvalid: Boolean
        get() = isDoseEmpty || isStockExceeded

    val hasError: Boolean
        get() = isDoseInvalid

    fun toTakenMedication(): TakenMedication =
        TakenMedication(
            medication = medication,
            dose = doseString.toBigDecimal()
        )
}