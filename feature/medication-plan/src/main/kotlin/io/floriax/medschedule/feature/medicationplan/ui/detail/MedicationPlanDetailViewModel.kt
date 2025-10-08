package io.floriax.medschedule.feature.medicationplan.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import io.floriax.medschedule.core.common.di.qualifier.IODispatcher
import io.floriax.medschedule.core.common.extension.logger
import io.floriax.medschedule.core.domain.usecase.DeleteMedicationPlanUseCase
import io.floriax.medschedule.core.domain.usecase.GetMedicationPlanByIdUseCase
import io.floriax.medschedule.core.domain.usecase.UpdateMedicationPlanUseCase
import io.floriax.medschedule.feature.medicationplan.navigation.MedicationPlanDetailRoute
import io.floriax.medschedule.shared.ui.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/10/8
 */
@HiltViewModel
class MedicationPlanDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMedicationPlanByIdUseCase: GetMedicationPlanByIdUseCase,
    private val updateMedicationPlanUseCase: UpdateMedicationPlanUseCase,
    private val deleteMedicationPlanUseCase: DeleteMedicationPlanUseCase,
    @param:IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel<MedicationPlanDetailUiState, MedicationPlanDetailSideEffect>() {

    private val logger by logger<MedicationPlanDetailViewModel>()

    override val initialState: MedicationPlanDetailUiState
        get() = MedicationPlanDetailUiState()

    private val medicationPlanId: Long =
        savedStateHandle.toRoute<MedicationPlanDetailRoute>().medicationPlanId

    init {
        viewModelScope.launch {
            runCatching {
                withContext(ioDispatcher) {
                    getMedicationPlanByIdUseCase(medicationPlanId)
                }
            }.onSuccess { plan ->
                if (plan != null) {
                    reduce { copy(loading = false, medicationPlan = plan) }
                } else {
                    reduce { copy(loading = false, error = true) }
                }
            }.onFailure { ex ->
                logger.e(ex, "Failed to get medication plan")
                reduce { copy(loading = false, error = true) }
            }
        }
    }

    fun onStatusChange(isActive: Boolean) {
        viewModelScope.launch {
            currentState.medicationPlan?.let { plan ->
                val updatedPlan = plan.copy(active = isActive)
                runCatching {
                    withContext(ioDispatcher) {
                        updateMedicationPlanUseCase(updatedPlan)
                    }
                }
                    .onSuccess { success ->
                        if (success) {
                            reduce { copy(medicationPlan = updatedPlan) }
                        } else {
                            logger.e("Failed to update plan status")
                            reduce { copy(medicationPlan = plan) }
                        }
                    }
                    .onFailure { ex ->
                        logger.e(ex, "Failed to update plan status")
                        reduce { copy(medicationPlan = plan) }
                    }
            }
        }
    }

    fun toggleDeleteDialog(show: Boolean) {
        reduce { copy(showDeleteDialog = show) }
    }

    fun attemptDeletePlan() {
        viewModelScope.launch {
            reduce { copy(showDeleteDialog = false) }
            currentState.medicationPlan?.let { plan ->
                runCatching {
                    withContext(ioDispatcher) {
                        deleteMedicationPlanUseCase(plan)
                    }
                }.onSuccess {
                    postSideEffect(DeletePlanSuccess)
                }.onFailure { ex ->
                    logger.e(ex, "Failed to delete plan")
                    postSideEffect(DeletePlanFailure)
                }
            }
        }
    }
}
