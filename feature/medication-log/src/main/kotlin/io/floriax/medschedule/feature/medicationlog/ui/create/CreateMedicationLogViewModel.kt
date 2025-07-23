package io.floriax.medschedule.feature.medicationlog.ui.create

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.floriax.medschedule.core.common.di.qualifier.IODispatcher
import io.floriax.medschedule.core.common.extension.logger
import io.floriax.medschedule.core.common.extension.mapAt
import io.floriax.medschedule.core.domain.enums.MedicationLogType
import io.floriax.medschedule.core.domain.enums.MedicationState
import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.core.domain.model.MedicationLog
import io.floriax.medschedule.core.domain.usecase.CreateMedicationLogUseCase
import io.floriax.medschedule.core.domain.usecase.ObservePagedMedicationsUseCase
import io.floriax.medschedule.core.domain.usecase.invoke
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
class CreateMedicationLogViewModel @Inject constructor(
    observePagedMedicationsUseCase: ObservePagedMedicationsUseCase,
    private val createMedicationLogUseCase: CreateMedicationLogUseCase,
    @param:IODispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<CreateMedicationLogUiState, CreateMedicationLogSideEffect>() {

    private val logger by logger<CreateMedicationLogViewModel>()

    override val initialState: CreateMedicationLogUiState
        get() = CreateMedicationLogUiState()

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
                takenMedicationInputs = takenMedicationInputs.mapAt(index) { input ->
                    val updated = input.copy(doseString = doseString)
                    updated.copy(isMarkedAsError = updated.hasError)
                }
            )
        }
    }

    fun onDeductFromStockCheckedChange(index: Int, deductFromStock: Boolean) {
        reduce {
            copy(
                takenMedicationInputs = takenMedicationInputs.mapAt(index) { input ->
                    input.copy(deductFromStock = deductFromStock)
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

    fun attemptCreateMedicationLog() {
        viewModelScope.launch {
            val takenMedicationInputs = currentState.takenMedicationInputs
            if (takenMedicationInputs.isEmpty()) {
                postSideEffect(TakenMedicationsEmpty)
                return@launch
            }

            var takenMedicationHasError = false
            val updatedTakenMedicationInputs = mutableListOf<TakenMedicationInput>()
            for (takenMedicationInput in takenMedicationInputs) {
                if (takenMedicationInput.hasError) {
                    takenMedicationHasError = true
                    if (!takenMedicationInput.isMarkedAsError) {
                        updatedTakenMedicationInputs += takenMedicationInput.copy(isMarkedAsError = true)
                        continue
                    }
                }
                updatedTakenMedicationInputs += takenMedicationInput
            }

            if (takenMedicationHasError) {
                reduce {
                    copy(takenMedicationInputs = updatedTakenMedicationInputs)
                }
                return@launch
            }

            val localDateTime =
                LocalDateTime.of(currentState.selectedDate, currentState.selectedTime)
            val timeZone = ZoneId.systemDefault()
            val medicationTime = localDateTime.atZone(timeZone).toInstant()
            val takenMedications = takenMedicationInputs.map { input -> input.toTakenMedication() }

            val log = MedicationLog(
                medicationTime = medicationTime,
                takenMedications = takenMedications,
                state = MedicationState.TAKEN,
                type = MedicationLogType.MANUAL,
                timeZone = timeZone,
                notes = currentState.notes
            )

            runCatching {
                withContext(ioDispatcher) {
                    createMedicationLogUseCase(log)
                }
            }
                .onSuccess { medicationLog ->
                    logger.d("Created medication log: $medicationLog")
                    postSideEffect(CreateMedicationLogSuccess(medicationLog))
                }
                .onFailure { ex ->
                    logger.e(ex, "Failed to create medication log")
                    postSideEffect(CreateMedicationLogFailure)
                }
        }
    }
}