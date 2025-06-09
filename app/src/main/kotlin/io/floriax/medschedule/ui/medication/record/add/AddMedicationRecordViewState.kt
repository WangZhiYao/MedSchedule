package io.floriax.medschedule.ui.medication.record.add

import io.floriax.medschedule.domain.model.Medication
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
    val selectedMedication: Medication? = null,
    val medicationError: Boolean = false,
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = LocalTime.now(),
    val doseString: String = "",
    val doseError: Boolean = false,
    val remark: String = ""
)