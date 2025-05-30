package io.floriax.medschedule.ui.home

import dagger.hilt.android.lifecycle.HiltViewModel
import io.floriax.medschedule.common.base.BaseViewModel
import javax.inject.Inject

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeViewState, HomeSideEffect>() {

    override val initialState: HomeViewState
        get() = HomeViewState()

}