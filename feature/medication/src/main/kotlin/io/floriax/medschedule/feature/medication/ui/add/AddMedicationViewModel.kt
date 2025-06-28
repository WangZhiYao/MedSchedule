package io.floriax.medschedule.feature.medication.ui.add

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.floriax.medschedule.core.common.di.qualifier.IODispatcher
import io.floriax.medschedule.core.common.extension.isValidStock
import io.floriax.medschedule.core.common.extension.logger
import io.floriax.medschedule.domain.model.Medication
import io.floriax.medschedule.domain.usecase.AddMedicationUseCase
import io.floriax.medschedule.shared.ui.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/27
 */
@HiltViewModel
class AddMedicationViewModel @Inject constructor(
    private val addMedicationUseCase: AddMedicationUseCase,
    @param:IODispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<AddMedicationViewState, AddMedicationSideEffect>() {

    private val logger by logger<AddMedicationViewModel>()

    override val initialState: AddMedicationViewState
        get() = AddMedicationViewState()

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
        val medicationName = currentState.medicationName
        if (medicationName.isBlank()) {
            reduce {
                copy(medicationNameError = true)
            }
            postSideEffect(RequestFocusOnNameField)
            return
        }

        val stockString = currentState.stockString
        if (stockString.isNotBlank() && !stockString.isValidStock()) {
            reduce {
                copy(stockError = true)
            }
            postSideEffect(RequestFocusOnStockField)
            return
        }

        val stock = stockString.toFloatOrNull() ?: DEFAULT_STOCK

        val doseUnit = currentState.doseUnit
        if (doseUnit.isBlank()) {
            reduce {
                copy(doseUnitError = true)
            }
            postSideEffect(RequestFocusOnDoseUnitField)
            return
        }

        val notes = currentState.notes

        viewModelScope.launch {
            flow {
                emit(
                    addMedicationUseCase(
                        Medication(
                            name = medicationName,
                            stock = stock,
                            doseUnit = doseUnit,
                            notes = notes
                        )
                    )
                )
            }
                .flowOn(ioDispatcher)
                .catch { ex ->
                    logger.e(ex, "Add medication failed")
                    postSideEffect(AddMedicationFailure)
                }
                .collect { medication ->
                    logger.d("Add medication success: $medication")
                    postSideEffect(AddMedicationSuccess(medication))
                }
        }
    }

    companion object {

        private const val DEFAULT_STOCK = -1f

    }
}