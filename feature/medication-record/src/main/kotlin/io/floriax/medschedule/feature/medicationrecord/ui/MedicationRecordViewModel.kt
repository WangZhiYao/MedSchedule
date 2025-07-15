package io.floriax.medschedule.feature.medicationrecord.ui

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.floriax.medschedule.core.common.di.qualifier.IODispatcher
import io.floriax.medschedule.core.common.extension.logger
import io.floriax.medschedule.core.domain.model.MedicationRecord
import io.floriax.medschedule.core.domain.usecase.ObservePagedMedicationRecordsUseCase
import io.floriax.medschedule.shared.ui.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/2
 */
@HiltViewModel
class MedicationRecordViewModel @Inject constructor(
    private val observePagedMedicationRecordsUseCase: ObservePagedMedicationRecordsUseCase,
    @param:IODispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<MedicationRecordUiState, MedicationRecordSideEffect>() {

    private val logger by logger<MedicationRecordViewModel>()

    override val initialState: MedicationRecordUiState
        get() = MedicationRecordUiState()

    val pagedMedicationRecords: Flow<PagingData<MedicationRecord>> =
        observePagedMedicationRecordsUseCase()
            .flowOn(ioDispatcher)
            .cachedIn(viewModelScope)

}