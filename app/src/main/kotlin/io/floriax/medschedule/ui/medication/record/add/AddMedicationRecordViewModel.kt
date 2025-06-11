package io.floriax.medschedule.ui.medication.record.add

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.floriax.medschedule.common.base.BaseViewModel
import io.floriax.medschedule.common.di.qualifier.IODispatcher
import io.floriax.medschedule.common.ext.isValidDose
import io.floriax.medschedule.common.ext.logger
import io.floriax.medschedule.common.ext.removeAt
import io.floriax.medschedule.common.ext.replaceAt
import io.floriax.medschedule.domain.enums.MedicationRecordType
import io.floriax.medschedule.domain.enums.MedicationState
import io.floriax.medschedule.domain.model.MedicationRecord
import io.floriax.medschedule.domain.usecase.AddMedicationRecordUseCase
import io.floriax.medschedule.domain.usecase.AddTakenMedicationBatchUseCase
import io.floriax.medschedule.domain.usecase.ObserveMedicationsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
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
    private val addTakenMedicationBatchUseCase: AddTakenMedicationBatchUseCase,
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
                .collect { medications ->
                    reduce {
                        copy(medications = medications)
                    }
                    if (medications.isEmpty()) {
                        postSideEffect(EmptyMedication)
                    }
                }
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

    fun onDeleteTakenMedicationClick(index: Int) {
        reduce {
            val newList = takenMedications.removeAt(index)
            val validatedList = getValidatedTakenMedications(true, newList)
            copy(takenMedications = validatedList)
        }
    }

    fun onAddTakenMedicationClick() {
        reduce {
            copy(takenMedications = takenMedications + TakenMedicationItem())
        }
    }

    fun onTakenMedicationItemContentChange(index: Int, takenMedicationItem: TakenMedicationItem) {
        reduce {
            val newList = takenMedications.replaceAt(index, takenMedicationItem)
            val validatedList = getValidatedTakenMedications(true, newList)
            copy(takenMedications = validatedList)
        }
    }

    fun onRemarkChange(remark: String) {
        reduce {
            copy(remark = remark)
        }
    }

    fun attemptAddMedicationRecord() {
        val validatedList = getValidatedTakenMedications(false, currentState.takenMedications)
        if (validatedList.any { item -> item.hasError }) {
            reduce {
                copy(takenMedications = validatedList)
            }
            return
        }

        val date = currentState.date
        val time = currentState.time
        val timeZone = ZoneOffset.systemDefault()

        val medicationTime = date.atTime(time)
            .atZone(timeZone)
            .toInstant()

        val remark = currentState.remark

        viewModelScope.launch {
            flow {
                emit(
                    addMedicationRecordUseCase(
                        MedicationRecord(
                            medicationTime = medicationTime,
                            remark = remark,
                            state = MedicationState.TAKEN,
                            type = MedicationRecordType.MANUAL,
                            timeZone = timeZone
                        )
                    )
                )
            }
                .map { medicationRecord ->
                    addTakenMedicationBatchUseCase(
                        validatedList.map { item ->
                            item.toTakenMedication(medicationRecord.id)
                        }
                    )
                    medicationRecord
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

    private fun getValidatedTakenMedications(
        editing: Boolean,
        takenMedicationItems: List<TakenMedicationItem>
    ): List<TakenMedicationItem> {
        val duplicateMedications = takenMedicationItems
            .mapNotNull { item -> item.selectedMedication }
            .groupingBy { medication -> medication }
            .eachCount()
            .filter { it.value > 1 }
            .keys

        return takenMedicationItems.map { item ->
            val medication = item.selectedMedication

            val medicationError = when (medication) {
                null -> if (editing) null else NotSelected
                in duplicateMedications -> Duplicated
                else -> null
            }

            val doseError = when {
                editing && item.doseString.isBlank() -> false
                else -> !item.doseString.isValidDose()
            }

            item.copy(medicationError = medicationError, doseError = doseError)
        }
    }
}