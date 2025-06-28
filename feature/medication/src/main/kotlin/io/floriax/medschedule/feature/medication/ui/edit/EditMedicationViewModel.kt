package io.floriax.medschedule.feature.medication.ui.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import io.floriax.medschedule.core.common.di.qualifier.IODispatcher
import io.floriax.medschedule.core.common.extension.isValidStock
import io.floriax.medschedule.core.common.extension.logger
import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.core.domain.usecase.GetMedicationByIdUseCase
import io.floriax.medschedule.core.domain.usecase.UpdateMedicationUseCase
import io.floriax.medschedule.feature.medication.navigation.EditMedicationRoute
import io.floriax.medschedule.shared.ui.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/28
 */
@HiltViewModel
class EditMedicationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMedicationUseCase: GetMedicationByIdUseCase,
    private val updateMedicationUseCase: UpdateMedicationUseCase,
    @param:IODispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<EditMedicationViewState, EditMedicationSideEffect>() {

    private val logger by logger<EditMedicationViewModel>()

    override val initialState: EditMedicationViewState
        get() = EditMedicationViewState()

    private val medicationId: Long = savedStateHandle.toRoute<EditMedicationRoute>().medicationId

    init {
        viewModelScope.launch {
            runCatching {
                withContext(ioDispatcher) {
                    getMedicationUseCase(medicationId)
                }
            }
                .onSuccess { medication ->
                    if (medication == null) {
                        postSideEffect(MedicationNotFound)
                    } else {
                        reduce {
                            copy(
                                originalMedication = medication,
                                medicationName = medication.name,
                                stockString = medication.stock?.toPlainString() ?: "",
                                doseUnit = medication.doseUnit,
                                notes = medication.notes
                            )
                        }
                    }
                }
                .onFailure { ex ->
                    logger.e(ex, "Get medication failed")
                    postSideEffect(GetMedicationFailure)
                }
        }
    }

    fun onMedicationNameChange(medicationName: String) {
        reduce {
            copy(medicationName = medicationName, medicationNameError = medicationName.isBlank())
        }
    }

    fun onStockStringChange(stockString: String) {
        reduce {
            copy(
                stockString = stockString,
                stockError = stockString.isNotBlank() && !stockString.isValidStock()
            )
        }
    }

    fun onDoseUnitChange(doseUnit: String) {
        reduce {
            copy(doseUnit = doseUnit, doseUnitError = doseUnit.isBlank())
        }
    }

    fun onNotesChange(notes: String) {
        reduce {
            copy(notes = notes)
        }
    }

    fun onSaveClick() {
        viewModelScope.launch {
            if (!validateInputs()) {
                return@launch
            }

            if (!currentState.hasContentChanged()) {
                currentState.originalMedication?.let { medication ->
                    postSideEffect(UpdateMedicationSuccess(medication))
                    return@launch
                }
            }

            val medicationName = currentState.medicationName
            val stock = currentState.stockString.toBigDecimalOrNull()
            val doseUnit = currentState.doseUnit
            val notes = currentState.notes

            runCatching {
                withContext(ioDispatcher) {
                    updateMedicationUseCase(
                        Medication(
                            id = medicationId,
                            name = medicationName,
                            stock = stock,
                            doseUnit = doseUnit,
                            notes = notes
                        )
                    )
                }
            }
                .onSuccess { medication ->
                    postSideEffect(UpdateMedicationSuccess(medication))
                }
                .onFailure { ex ->
                    logger.e(ex, "Update medication failed")
                    postSideEffect(UpdateMedicationFailure)
                }
        }
    }

    private suspend fun validateInputs(): Boolean {
        val nameError = currentState.medicationName.isBlank()
        val stockError = currentState.stockString.isNotBlank()
                && !currentState.stockString.isValidStock()
        val doseUnitError = currentState.doseUnit.isBlank()

        reduce {
            copy(
                medicationNameError = nameError,
                stockError = stockError,
                doseUnitError = doseUnitError
            )
        }

        when {
            nameError -> postSideEffect(RequestFocusOnNameField)
            stockError -> postSideEffect(RequestFocusOnStockField)
            doseUnitError -> postSideEffect(RequestFocusOnDoseUnitField)
        }

        return !nameError && !stockError && !doseUnitError
    }
}