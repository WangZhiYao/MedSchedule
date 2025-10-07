package io.floriax.medschedule.feature.medicationplan.ui

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.floriax.medschedule.core.domain.usecase.ObserveMedicationPlansUseCase
import io.floriax.medschedule.shared.ui.base.BaseViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/10/6
 */
@HiltViewModel
class MedicationPlanViewModel @Inject constructor(
    observeMedicationPlansUseCase: ObserveMedicationPlansUseCase
) : BaseViewModel<MedicationPlanUiState, Unit>() {

    override val initialState: MedicationPlanUiState
        get() = MedicationPlanUiState()

    init {
        observeMedicationPlansUseCase()
            .onEach { plans ->
                val groupedPlans = plans.groupBy { it.active }
                val activePlans = (groupedPlans[true] ?: emptyList())
                    .sortedByDescending { it.createdAt }
                val inactivePlans = (groupedPlans[false] ?: emptyList())
                    .sortedByDescending { it.createdAt }

                reduce {
                    copy(
                        activePlans = activePlans,
                        inactivePlans = inactivePlans
                    )
                }
            }
            .launchIn(viewModelScope)
    }

}
