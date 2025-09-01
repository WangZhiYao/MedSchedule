package io.floriax.medschedule.feature.medication.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.floriax.medschedule.core.common.di.qualifier.IODispatcher
import io.floriax.medschedule.core.common.extension.logger
import io.floriax.medschedule.core.domain.model.MedicationLog
import io.floriax.medschedule.core.domain.usecase.DeleteMedicationUseCase
import io.floriax.medschedule.core.domain.usecase.ObserveMedicationByIdUseCase
import io.floriax.medschedule.core.domain.usecase.ObservePagedMedicationLogByMedicationUseCase
import io.floriax.medschedule.core.domain.usecase.UpdateMedicationUseCase
import io.floriax.medschedule.feature.medication.navigation.EditMedicationRoute
import io.floriax.medschedule.shared.ui.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/10
 */
@HiltViewModel
class MedicationDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val observeMedicationByIdUseCase: ObserveMedicationByIdUseCase,
    observePagedMedicationLogByMedicationUseCase: ObservePagedMedicationLogByMedicationUseCase,
    private val deleteMedicationUseCase: DeleteMedicationUseCase,
    private val updateMedicationUseCase: UpdateMedicationUseCase,
    @param:IODispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<MedicationDetailUiState, MedicationDetailSideEffect>() {

    private val logger by logger<MedicationDetailViewModel>()

    override val initialState: MedicationDetailUiState
        get() = MedicationDetailUiState()

    val medicationId: Long = savedStateHandle.toRoute<EditMedicationRoute>().medicationId

    val medicationLogs: Flow<PagingData<MedicationLog>> =
        observePagedMedicationLogByMedicationUseCase(medicationId)
            .cachedIn(viewModelScope)
            .catch { ex ->
                logger.e(ex, "Error while observing medication logs")
            }

    init {
        viewModelScope.launch {
            observeMedicationByIdUseCase(medicationId)
                .flowOn(ioDispatcher)
                .onStart {
                    reduce {
                        copy(loading = true, error = false)
                    }
                }
                .catch { ex ->
                    logger.e(ex, "Error while observing medication")
                    reduce {
                        copy(loading = false, error = true)
                    }
                }
                .collect { medication ->
                    reduce {
                        copy(loading = false, error = false, medication = medication)
                    }
                }
        }
    }

    fun toggleDeleteDialog(show: Boolean) {
        reduce {
            copy(showDeleteDialog = show)
        }
    }

    fun toggleAddStockBottomSheet(show: Boolean) {
        reduce {
            copy(showAddStockBottomSheet = show)
        }
    }

    fun attemptDeleteMedication() {
        viewModelScope.launch {
            reduce {
                copy(showDeleteDialog = false)
            }
            val medication = currentState.medication ?: return@launch
            runCatching {
                withContext(ioDispatcher) {
                    deleteMedicationUseCase(medication)
                }
            }
                .onSuccess {
                    postSideEffect(DeleteMedicationSuccess)
                }
                .onFailure { ex ->
                    logger.e(ex, "Error while deleting medication")
                    postSideEffect(DeleteMedicationFailure)
                }
        }
    }

    fun attemptAddStock(quantityToAdd: BigDecimal) {
        viewModelScope.launch {
            val medication = currentState.medication ?: return@launch
            val currentStock = medication.stock ?: BigDecimal.ZERO
            runCatching {
                withContext(ioDispatcher) {
                    updateMedicationUseCase(medication.copy(stock = currentStock + quantityToAdd))
                }
            }
                .onSuccess { medication ->
                    postSideEffect(AddStockSuccess)
                }
                .onFailure { ex ->
                    logger.e(ex, "Error while adding stock")
                    postSideEffect(AddStockFailure)
                }
        }
    }
}