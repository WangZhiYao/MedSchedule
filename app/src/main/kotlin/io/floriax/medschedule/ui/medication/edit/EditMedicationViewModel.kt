package io.floriax.medschedule.ui.medication.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import io.floriax.medschedule.common.base.BaseViewModel
import io.floriax.medschedule.common.di.qualifier.IODispatcher
import io.floriax.medschedule.common.ext.logger
import io.floriax.medschedule.domain.model.Medication
import io.floriax.medschedule.domain.usecase.GetMedicationByIdUseCase
import io.floriax.medschedule.domain.usecase.UpdateMedicationUseCase
import io.floriax.medschedule.navigation.Route
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/31
 */
@HiltViewModel
class EditMedicationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMedicationByIdUseCase: GetMedicationByIdUseCase,
    private val updateMedicationUseCase: UpdateMedicationUseCase,
    @param:IODispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<EditMedicationViewState, EditMedicationSideEffect>() {

    private val logger by logger<EditMedicationViewModel>()

    override val initialState: EditMedicationViewState
        get() = EditMedicationViewState()

    init {
        viewModelScope.launch {
            flow {
                emit(savedStateHandle.toRoute<Route.EditMedication>().id)
            }
                .map(getMedicationByIdUseCase::invoke)
                .flowOn(ioDispatcher)
                .catch { ex ->
                    logger.e(ex, "Error getting medication by id")
                    reduce {
                        copy(error = true)
                    }
                }
                .collect { medication ->
                    if (medication == null) {
                        reduce {
                            copy(error = true)
                        }
                    } else {
                        reduce {
                            copy(
                                id = medication.id,
                                name = medication.name,
                                doseUnit = medication.doseUnit,
                                remark = medication.remark
                            )
                        }
                    }
                }
        }
    }

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

    fun attemptUpdateMedication() {
        if (currentState.nameError) {
            return
        }

        val id = currentState.id
        val name = currentState.name
        val doseUnit = currentState.doseUnit
        val remark = currentState.remark

        viewModelScope.launch {
            flow {
                emit(
                    updateMedicationUseCase(
                        Medication(
                            id = id,
                            name = name,
                            doseUnit = doseUnit,
                            remark = remark
                        )
                    )
                )
            }
                .flowOn(ioDispatcher)
                .catch { ex ->
                    logger.e(ex, "Error updating medication")
                    postSideEffect(EditMedicationFailed)
                }
                .collect { medication ->
                    postSideEffect(EditMedicationSuccess)
                }
        }
    }
}