package io.floriax.medschedule.feature.medication.ui

import io.floriax.medschedule.core.domain.model.Medication

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/27
 */
data class MedicineCabinetViewState(
    val medicationToDelete: Medication? = null,
)