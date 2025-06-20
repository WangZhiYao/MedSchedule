package io.floriax.medschedule.ui.medication

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.floriax.medschedule.common.base.BaseViewModel
import io.floriax.medschedule.common.di.qualifier.IODispatcher
import io.floriax.medschedule.common.ext.logger
import io.floriax.medschedule.domain.model.Medication
import io.floriax.medschedule.domain.usecase.DeleteMedicationUseCase
import io.floriax.medschedule.domain.usecase.ObserveMedicationsUseCase
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
 * @since 2025/5/30
 */
@HiltViewModel
class MedicationListViewModel @Inject constructor(
    private val observeMedicationsUseCase: ObserveMedicationsUseCase,
    private val deleteMedicationUseCase: DeleteMedicationUseCase,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<MedicationListViewState, MedicationListSideEffect>() {

    private val logger by logger<MedicationListViewModel>()

    override val initialState: MedicationListViewState
        get() = MedicationListViewState()

    init {
        viewModelScope.launch {
            observeMedicationsUseCase()
                .flowOn(ioDispatcher)
                .catch { ex ->
                    logger.e(ex, "Error observing medication list")
                }
                .collect { medicationList ->
                    reduce {
                        copy(medications = medicationList)
                    }
                }
        }
    }

    fun attemptDeleteMedication(medication: Medication) {
        viewModelScope.launch {
            flow {
                emit(deleteMedicationUseCase(medication))
            }
                .flowOn(ioDispatcher)
                .catch { ex ->
                    logger.e(ex, "Error deleting medication")
                    emit(false)
                }
                .collect { success ->
                    if (success) {
                        postSideEffect(DeleteMedicationSuccess)
                    } else {
                        postSideEffect(DeleteMedicationFailed)
                    }
                }
        }
    }
}