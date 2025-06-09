package io.floriax.medschedule.ui.medication.record.add

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.floriax.medschedule.common.base.BaseViewModel
import io.floriax.medschedule.common.di.qualifier.IODispatcher
import io.floriax.medschedule.common.ext.isValidDose
import io.floriax.medschedule.common.ext.logger
import io.floriax.medschedule.domain.enums.MedicationRecordType
import io.floriax.medschedule.domain.enums.MedicationState
import io.floriax.medschedule.domain.model.Medication
import io.floriax.medschedule.domain.model.MedicationRecord
import io.floriax.medschedule.domain.usecase.AddMedicationRecordUseCase
import io.floriax.medschedule.domain.usecase.ObserveMedicationsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/31
 */
@HiltViewModel
class AddMedicationRecordViewModel @Inject constructor(
    private val observeMedicationsUseCase: ObserveMedicationsUseCase,
    private val addMedicationRecordUseCase: AddMedicationRecordUseCase,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel<AddMedicationRecordViewState, AddMedicationRecordSideEffect>() {

    private val logger by logger<AddMedicationRecordViewModel>()

    override val initialState: AddMedicationRecordViewState
        get() = AddMedicationRecordViewState()

    init {
        viewModelScope.launch {
            observeMedicationsUseCase()
                .flowOn(ioDispatcher)
                .catch { ex ->
                    logger.e("Error observing medication list", ex)
                }
                .collect { medicationList ->
                    reduce {
                        copy(
                            medications = medicationList,
                            selectedMedication = medicationList.firstOrNull()
                        )
                    }
                    if (medicationList.isEmpty()) {
                        postSideEffect(EmptyMedication)
                    }
                }
        }
    }

    fun onMedicationSelect(medication: Medication) {
        reduce {
            copy(selectedMedication = medication)
        }
    }

    fun onDateChange(date: LocalDate) {
        reduce {
            copy(date = date)
        }
    }

    fun onTimeChange(time: LocalTime) {
        reduce {
            copy(time = time)
        }
    }

    fun onDoseChange(doseString: String) {
        reduce {
            copy(
                doseString = doseString,
                doseError = if (doseString.isNotEmpty()) !doseString.isValidDose() else false
            )
        }
    }

    fun onRemarkChange(remark: String) {
        reduce {
            copy(remark = remark)
        }
    }

    fun attemptAddMedicationRecord() {
        val medication = currentState.selectedMedication
        if (medication == null) {
            reduce { copy(medicationError = true) }
            return
        }

        val date = currentState.date
        val time = currentState.time

        val timeZone = ZoneOffset.systemDefault()

        val medicationTime = LocalDateTime.of(date, time)
            .atZone(timeZone)
            .toInstant()

        val doseString = currentState.doseString
        if (!doseString.isValidDose()) {
            reduce { copy(doseError = true) }
            return
        }

        val remark = currentState.remark

        viewModelScope.launch {
            flow {
                emit(
                    addMedicationRecordUseCase(
                        MedicationRecord(
                            medicationId = medication.id,
                            medicationTime = medicationTime,
                            dose = doseString.toFloat(),
                            remark = remark,
                            state = MedicationState.TAKEN,
                            type = MedicationRecordType.MANUAL,
                            timeZone = timeZone
                        )
                    )
                )
            }
                .flowOn(ioDispatcher)
                .catch { ex ->
                    logger.e("Error adding medication record", ex)
                    postSideEffect(AddMedicationRecordFailed)
                }
                .collect { medicationRecord ->
                    postSideEffect(AddMedicationRecordSuccess)
                }
        }
    }
}