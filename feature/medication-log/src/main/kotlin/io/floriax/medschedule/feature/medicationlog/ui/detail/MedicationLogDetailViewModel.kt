package io.floriax.medschedule.feature.medicationlog.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import io.floriax.medschedule.core.common.di.qualifier.IODispatcher
import io.floriax.medschedule.core.common.extension.logger
import io.floriax.medschedule.core.domain.usecase.DeleteMedicationLogUseCase
import io.floriax.medschedule.core.domain.usecase.ObserveMedicationLogByIdUseCase
import io.floriax.medschedule.feature.medicationlog.navigation.MedicationLogDetailRoute
import io.floriax.medschedule.shared.ui.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/14
 */
@HiltViewModel
class MedicationLogDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeMedicationLogByIdUseCase: ObserveMedicationLogByIdUseCase,
    private val deleteMedicationLogUseCase: DeleteMedicationLogUseCase,
    @param:IODispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<MedicationLogDetailUiState, MedicationLogDetailSideEffect>() {

    private val logger by logger<MedicationLogDetailViewModel>()

    override val initialState: MedicationLogDetailUiState
        get() = MedicationLogDetailUiState()

    private val medicationLogId: Long =
        savedStateHandle.toRoute<MedicationLogDetailRoute>().medicationLogId

    init {
        viewModelScope.launch {
            observeMedicationLogByIdUseCase(medicationLogId)
                .flowOn(ioDispatcher)
                .onStart {
                    reduce { copy(loading = true) }
                }
                .catch { ex ->
                    logger.e(ex, "Observe medication log failed")
                    reduce { copy(loading = false, error = true) }
                }
                .collect { medicationLog ->
                    reduce {
                        copy(
                            loading = false,
                            error = false,
                            medicationLog = medicationLog
                        )
                    }
                }
        }
    }

    fun toggleDeleteDialog(show: Boolean) {
        reduce {
            copy(showDeleteDialog = show)
        }
    }

    fun attemptDeleteMedicationLog() {
        reduce {
            copy(showDeleteDialog = false)
        }
        viewModelScope.launch {
            val medicationLog = currentState.medicationLog ?: return@launch
            runCatching {
                withContext(ioDispatcher) {
                    deleteMedicationLogUseCase(medicationLog)
                }
            }
                .onSuccess {
                    postSideEffect(DeleteMedicationLogSuccess)
                }
                .onFailure { ex ->
                    logger.e(ex, "Delete medication log failed")
                    postSideEffect(DeleteMedicationLogFailure)
                }
        }
    }
}