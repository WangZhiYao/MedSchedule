package io.floriax.medschedule.ui.medication.add

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.floriax.medschedule.common.base.BaseViewModel
import io.floriax.medschedule.common.di.qualifier.IODispatcher
import io.floriax.medschedule.common.ext.logger
import io.floriax.medschedule.domain.model.Medication
import io.floriax.medschedule.domain.usecase.AddMedicationUseCase
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
 * @since 2025/5/31
 */
@HiltViewModel
class AddMedicationViewModel @Inject constructor(
    private val addMedicationUseCase: AddMedicationUseCase,
    @param:IODispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<AddMedicationViewState, AddMedicationSideEffect>() {

    private val logger by logger<AddMedicationViewModel>()

    override val initialState: AddMedicationViewState
        get() = AddMedicationViewState()

    fun onNameChange(name: String) {
        reduce {
            copy(name = name, nameError = name.isBlank())
        }
    }

    fun onDoseUnitChange(doseUnit: String) {
        reduce {
            copy(doseUnit = doseUnit)
        }
    }

    fun onRemarkChange(remark: String) {
        reduce {
            copy(remark = remark)
        }
    }

    fun attemptAddMedication() {
        if (currentState.nameError) {
            return
        }

        val name = currentState.name
        val doseUnit = currentState.doseUnit
        val remark = currentState.remark

        viewModelScope.launch {
            flow {
                emit(
                    addMedicationUseCase(
                        Medication(name = name, doseUnit = doseUnit, remark = remark)
                    )
                )
            }
                .flowOn(ioDispatcher)
                .catch { ex ->
                    logger.e(ex, "Error adding medication")
                    postSideEffect(AddMedicationFailed)
                }
                .collect { medication ->
                    postSideEffect(AddMedicationSuccess)
                }
        }
    }
}