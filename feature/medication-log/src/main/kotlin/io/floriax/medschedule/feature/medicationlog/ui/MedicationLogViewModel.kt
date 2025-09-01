package io.floriax.medschedule.feature.medicationlog.ui

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.floriax.medschedule.core.common.extension.logger
import io.floriax.medschedule.core.domain.model.MedicationLog
import io.floriax.medschedule.core.domain.usecase.ObservePagedMedicationLogsUseCase
import io.floriax.medschedule.shared.ui.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/2
 */
@HiltViewModel
class MedicationLogViewModel @Inject constructor(
    observePagedMedicationLogsUseCase: ObservePagedMedicationLogsUseCase,
) : BaseViewModel<MedicationLogUiState, MedicationLogSideEffect>() {

    private val logger by logger<MedicationLogViewModel>()

    override val initialState: MedicationLogUiState
        get() = MedicationLogUiState()

    val pagedMedicationLogs: Flow<PagingData<MedicationLog>> =
        observePagedMedicationLogsUseCase()
            .cachedIn(viewModelScope)

}