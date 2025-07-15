package io.floriax.medschedule.feature.medication.ui

import io.floriax.medschedule.shared.ui.base.UiState

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/27
 */
data class MedicineCabinetUiState(
    val error: Boolean = false,
) : UiState