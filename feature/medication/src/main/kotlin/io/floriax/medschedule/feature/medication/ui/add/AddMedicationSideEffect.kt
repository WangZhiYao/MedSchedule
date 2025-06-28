package io.floriax.medschedule.feature.medication.ui.add

import io.floriax.medschedule.domain.model.Medication

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/27
 */
sealed class AddMedicationSideEffect

data class AddMedicationSuccess(val medication: Medication) : AddMedicationSideEffect()

data object AddMedicationFailure : AddMedicationSideEffect()

data object RequestFocusOnNameField : AddMedicationSideEffect()

data object RequestFocusOnStockField : AddMedicationSideEffect()

data object RequestFocusOnDoseUnitField : AddMedicationSideEffect()
