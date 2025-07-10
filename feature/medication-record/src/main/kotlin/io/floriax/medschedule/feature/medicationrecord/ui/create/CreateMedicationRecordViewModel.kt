package io.floriax.medschedule.feature.medicationrecord.ui.create

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.floriax.medschedule.core.common.di.qualifier.IODispatcher
import io.floriax.medschedule.core.common.extension.logger
import io.floriax.medschedule.core.domain.enums.MedicationRecordType
import io.floriax.medschedule.core.domain.enums.MedicationState
import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.core.domain.model.MedicationRecord
import io.floriax.medschedule.core.domain.usecase.CreateMedicationRecordUseCase
import io.floriax.medschedule.core.domain.usecase.ObservePagedMedicationsUseCase
import io.floriax.medschedule.shared.ui.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/2
 */
@HiltViewModel
class CreateMedicationRecordViewModel @Inject constructor(
    observePagedMedicationsUseCase: ObservePagedMedicationsUseCase,
    private val createMedicationRecordUseCase: CreateMedicationRecordUseCase,
    @param:IODispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<CreateMedicationRecordViewState, CreateMedicationRecordSideEffect>() {

    private val logger by logger<CreateMedicationRecordViewModel>()

    override val initialState: CreateMedicationRecordViewState
        get() = CreateMedicationRecordViewState()

    val pagedMedications: Flow<PagingData<Medication>> =
        observePagedMedicationsUseCase()
            .flowOn(ioDispatcher)
            .cachedIn(viewModelScope)

    fun toggleDatePickerDialog(show: Boolean) {
        reduce {
            copy(showSelectDateDialog = show)
        }
    }

    fun onDateSelected(date: LocalDate) {
        reduce {
            copy(
                showSelectDateDialog = false,
                selectedDate = date
            )
        }
    }

    fun toggleTimePickerDialog(show: Boolean) {
        reduce {
            copy(showSelectTimeDialog = show)
        }
    }

    fun onTimeSelected(time: LocalTime) {
        reduce {
            copy(
                showSelectTimeDialog = false,
                selectedTime = time
            )
        }
    }

    fun toggleAddMedicationDialog(show: Boolean) {
        reduce {
            copy(showAddMedicationDialog = show)
        }
    }

    fun confirmAddMedication() {
        viewModelScope.launch {
            reduce {
                copy(showAddMedicationDialog = false)
            }
            postSideEffect(NavigateToAddMedication)
        }
    }

    fun toggleAddMedicationBottomSheet(show: Boolean) {
        reduce {
            copy(showSelectMedicationBottomSheet = show)
        }
    }

    fun onAddTakenMedication(medication: Medication) {
        reduce {
            copy(
                takenMedicationInputs = takenMedicationInputs + TakenMedicationInput(
                    medication = medication,
                    doseString = ""
                )
            )
        }
    }

    fun onDoseChange(index: Int, doseString: String) {
        reduce {
            copy(
                takenMedicationInputs = takenMedicationInputs.mapIndexed { i, takenMedication ->
                    if (i == index) {
                        with(takenMedication) {
                            copy(
                                doseString = doseString,
                                error = doseString.toBigDecimalOrNull() == null
                                        || (medication.stock != null && doseString.toBigDecimal() > medication.stock)
                            )
                        }
                    } else {
                        takenMedication
                    }
                }
            )
        }
    }

    fun onRemoveTakenMedication(medication: Medication) {
        reduce {
            copy(
                takenMedicationInputs = takenMedicationInputs.filterNot { takenMedication ->
                    takenMedication.medication == medication
                }
            )
        }
    }

    fun onNotesChange(notes: String) {
        reduce {
            copy(notes = notes)
        }
    }

    fun attemptCreateMedicationRecord() {
        viewModelScope.launch {
            val takenMedicationInputs = currentState.takenMedicationInputs
            if (takenMedicationInputs.isEmpty()) {
                postSideEffect(TakenMedicationsEmpty)
                return@launch
            }

            val takenMedicationHasError = takenMedicationInputs.any { takenMedication ->
                takenMedication.hasError
            }
            if (takenMedicationHasError) {
                return@launch
            }

            val localDateTime =
                LocalDateTime.of(currentState.selectedDate, currentState.selectedTime)
            val timeZone = ZoneId.systemDefault()
            val medicationTime = localDateTime.atZone(timeZone).toInstant()
            val takenMedications = takenMedicationInputs.map { input -> input.toTakenMedication() }

            val record = MedicationRecord(
                medicationTime = medicationTime,
                takenMedications = takenMedications,
                state = MedicationState.TAKEN,
                type = MedicationRecordType.MANUAL,
                timeZone = timeZone,
                notes = currentState.notes
            )

            runCatching {
                withContext(ioDispatcher) {
                    createMedicationRecordUseCase(record)
                }
            }
                .onSuccess { medicationRecord ->
                    logger.d("Created medication record: $medicationRecord")
                    postSideEffect(CreateMedicationRecordSuccess(medicationRecord))
                }
                .onFailure { ex ->
                    logger.e(ex, "Failed to create medication record")
                    postSideEffect(CreateMedicationRecordFailure)
                }
        }
    }
}