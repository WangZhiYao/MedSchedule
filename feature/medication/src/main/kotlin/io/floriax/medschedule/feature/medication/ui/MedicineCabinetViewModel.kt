package io.floriax.medschedule.feature.medication.ui

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.floriax.medschedule.core.common.di.qualifier.IODispatcher
import io.floriax.medschedule.core.common.extension.logger
import io.floriax.medschedule.core.domain.model.Medication
import io.floriax.medschedule.core.domain.usecase.DeleteMedicationUseCase
import io.floriax.medschedule.core.domain.usecase.ObservePagedMedicationsUseCase
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
class MedicineCabinetViewModel @Inject constructor(
    private val observePagedMedicationsUseCase: ObservePagedMedicationsUseCase,
    private val deleteMedicationUseCase: DeleteMedicationUseCase,
    @param:IODispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<MedicineCabinetViewState, MedicineCabinetSideEffect>() {

    private val logger by logger<MedicineCabinetViewModel>()

    override val initialState: MedicineCabinetViewState
        get() = MedicineCabinetViewState()

    val pagedMedications = observePagedMedicationsUseCase()
        .flowOn(ioDispatcher)
        .cachedIn(viewModelScope)

    fun updateMedicationToDelete(medication: Medication?) {
        reduce {
            copy(medicationToDelete = medication)
        }
    }

    fun attemptDeleteMedication(medication: Medication) {
        viewModelScope.launch {
            flow {
                emit(deleteMedicationUseCase(medication))
            }
                .flowOn(ioDispatcher)
                .catch { ex ->
                    logger.e(ex, "Delete medication failed")
                    postSideEffect(DeleteMedicationFailure)
                }
                .collect {
                    reduce {
                        copy(medicationToDelete = null)
                    }
                    postSideEffect(DeleteMedicationSuccess)
                }
        }
    }
}