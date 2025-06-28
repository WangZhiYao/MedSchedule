package io.floriax.medschedule.feature.medication.ui.add

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.floriax.medschedule.core.common.di.qualifier.IODispatcher
import io.floriax.medschedule.core.common.extension.isValidStock
import io.floriax.medschedule.core.common.extension.logger
import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.core.domain.usecase.AddMedicationUseCase
import io.floriax.medschedule.shared.ui.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        if (!validateInputs()) {
            return
        }

        val medicationName = currentState.medicationName
        val stock = currentState.stockString.toBigDecimalOrNull()
        val doseUnit = currentState.doseUnit
        val notes = currentState.notes

        viewModelScope.launch {
            runCatching {
                withContext(ioDispatcher) {
                    addMedicationUseCase(
                        Medication(
                            name = medicationName,
                            stock = stock,
                            doseUnit = doseUnit,
                            notes = notes
                        )
                    )
                }
            }
                .onSuccess { medication ->
                    logger.d("Add medication success: $medication")
                    postSideEffect(AddMedicationSuccess(medication))
                }
                .onFailure { ex ->
                    logger.e(ex, "Add medication failed")
                    postSideEffect(AddMedicationFailure)
                }
        }
    }

    private fun validateInputs(): Boolean {
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