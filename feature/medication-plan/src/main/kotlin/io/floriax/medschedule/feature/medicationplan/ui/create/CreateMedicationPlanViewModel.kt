package io.floriax.medschedule.feature.medicationplan.ui.create

import dagger.hilt.android.lifecycle.HiltViewModel
import io.floriax.medschedule.shared.ui.base.BaseViewModel
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/25
 */
@HiltViewModel
class CreateMedicationPlanViewModel @Inject constructor(

) : BaseViewModel<CreateMedicationPlanUiState, CreateMedicationPlanSideEffect>() {

    override val initialState: CreateMedicationPlanUiState
        get() = CreateMedicationPlanUiState()

    fun onPreviousStepClick() {
        currentState.currentStep.previous()?.let { step ->
            reduce {
                copy(
                    currentStep = step,
                    showPreviousStep = step.canShowPrevious(),
                    showSaveButton = step.canShowSave()
                )
            }
        }
    }

    fun onNextStepClick() {
        currentState.currentStep.next()?.let { step ->
            reduce {
                copy(
                    currentStep = step,
                    showPreviousStep = step.canShowPrevious(),
                    showSaveButton = step.canShowSave()
                )
            }
        }
    }
}