package io.floriax.medschedule.feature.medicationrecord.ui.create

import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.core.domain.model.TakenMedication
import java.time.LocalDate
import java.time.LocalTime

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/2
 */
data class CreateMedicationRecordViewState(
    val showSelectDateDialog: Boolean = false,
    val selectedDate: LocalDate = LocalDate.now(),
    val showSelectTimeDialog: Boolean = false,
    val selectedTime: LocalTime = LocalTime.now(),
    val showAddMedicationDialog: Boolean = false,
    val takenMedicationInputs: List<TakenMedicationInput> = emptyList(),
    val showSelectMedicationBottomSheet: Boolean = false,
    val notes: String = ""
)

data class TakenMedicationInput(
    val medication: Medication,
    val doseString: String,
    val error: Boolean = false,
) {

    val hasError: Boolean
        get() = doseString.toBigDecimalOrNull() == null
                || (medication.stock != null && doseString.toBigDecimal() > medication.stock)
                || error

    fun toTakenMedication(): TakenMedication =
        TakenMedication(
            medication = medication,
            dose = doseString.toBigDecimal()
        )
}