package io.floriax.medschedule.feature.medication.ui.edit

import io.floriax.medschedule.core.domain.model.Medication

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/28
 */
sealed class EditMedicationSideEffect

data object MedicationNotFound : EditMedicationSideEffect()

data object GetMedicationFailure : EditMedicationSideEffect()

data class UpdateMedicationSuccess(val medication: Medication) : EditMedicationSideEffect()

data object UpdateMedicationFailure : EditMedicationSideEffect()

data object RequestFocusOnNameField : EditMedicationSideEffect()

data object RequestFocusOnStockField : EditMedicationSideEffect()

data object RequestFocusOnDoseUnitField : EditMedicationSideEffect()