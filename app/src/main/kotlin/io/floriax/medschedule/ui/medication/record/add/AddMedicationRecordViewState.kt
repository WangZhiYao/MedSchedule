package io.floriax.medschedule.ui.medication.record.add

import io.floriax.medschedule.domain.model.Medication
import io.floriax.medschedule.domain.model.TakenMedication
import java.time.LocalDate
import java.time.LocalTime

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/31
 */
data class AddMedicationRecordViewState(
    val medications: List<Medication> = emptyList(),
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = LocalTime.now(),
    val takenMedications: List<TakenMedicationItem> = listOf(TakenMedicationItem()),
    val remark: String = ""
)

data class TakenMedicationItem(
    val selectedMedication: Medication? = null,
    val medicationError: MedicationError? = null,
    val doseString: String = "",
    val doseError: Boolean = false,
) {

    val hasError: Boolean
        get() = medicationError != null || doseError

    fun toTakenMedication(recordId: Long): TakenMedication =
        TakenMedication(
            medicationId = selectedMedication!!.id,
            medicationRecordId = recordId,
            dose = doseString.toFloat()
        )

}

sealed interface MedicationError

data object NotSelected : MedicationError

data object Duplicated : MedicationError