package io.floriax.medschedule.shared.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
abstract class BaseViewModel<STATE : Any, SIDE_EFFECT : Any> : ViewModel() {

    abstract val initialState: STATE

    private val _state: MutableStateFlow<STATE> by lazy { MutableStateFlow(initialState) }
    private val _sideEffect: MutableSharedFlow<SIDE_EFFECT> = MutableSharedFlow()

    val state: StateFlow<STATE>
        get() = _state.asStateFlow()

    val sideEffect: SharedFlow<SIDE_EFFECT>
        get() = _sideEffect.asSharedFlow()

    protected val currentState: STATE
        get() = state.value

    protected fun reduce(reducer: STATE.() -> STATE) {
        _state.update(reducer)
    }

    protected fun postSideEffect(sideEffect: SIDE_EFFECT) {
        viewModelScope.launch {
            _sideEffect.emit(sideEffect)
        }
    }
}