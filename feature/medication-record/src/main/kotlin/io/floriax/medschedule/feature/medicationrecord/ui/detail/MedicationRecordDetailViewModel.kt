package io.floriax.medschedule.feature.medicationrecord.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import io.floriax.medschedule.core.common.di.qualifier.IODispatcher
import io.floriax.medschedule.core.common.extension.logger
import io.floriax.medschedule.core.domain.usecase.DeleteMedicationRecordUseCase
import io.floriax.medschedule.core.domain.usecase.ObserveMedicationRecordByIdUseCase
import io.floriax.medschedule.feature.medicationrecord.navigation.MedicationRecordDetailRoute
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
class MedicationRecordDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeMedicationRecordByIdUseCase: ObserveMedicationRecordByIdUseCase,
    private val deleteMedicationRecordUseCase: DeleteMedicationRecordUseCase,
    @param:IODispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<MedicationRecordDetailUiState, MedicationRecordDetailSideEffect>() {

    private val logger by logger<MedicationRecordDetailViewModel>()

    override val initialState: MedicationRecordDetailUiState
        get() = MedicationRecordDetailUiState()

    private val medicationRecordId: Long =
        savedStateHandle.toRoute<MedicationRecordDetailRoute>().medicationRecordId

    init {
        viewModelScope.launch {
            observeMedicationRecordByIdUseCase(medicationRecordId)
                .flowOn(ioDispatcher)
                .onStart {
                    reduce { copy(loading = true) }
                }
                .catch { ex ->
                    logger.e(ex, "Observe medication record failed")
                    reduce { copy(loading = false, error = true) }
                }
                .collect { medicationRecord ->
                    reduce {
                        copy(
                            loading = false,
                            error = false,
                            medicationRecord = medicationRecord
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

    fun attemptDeleteMedicationRecord() {
        reduce {
            copy(showDeleteDialog = false)
        }
        viewModelScope.launch {
            val medicationRecord = currentState.medicationRecord ?: return@launch
            runCatching {
                withContext(ioDispatcher) {
                    deleteMedicationRecordUseCase(medicationRecord)
                }
            }
                .onSuccess {
                    postSideEffect(DeleteMedicationRecordSuccess)
                }
                .onFailure { ex ->
                    logger.e(ex, "Delete medication record failed")
                    postSideEffect(DeleteMedicationRecordFailure)
                }
        }
    }
}