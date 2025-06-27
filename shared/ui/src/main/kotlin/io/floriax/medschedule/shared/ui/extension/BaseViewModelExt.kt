package io.floriax.medschedule.shared.ui.extension

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import io.floriax.medschedule.shared.ui.base.BaseViewModel

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
@Composable
fun <STATE : Any, SIDE_EFFECT : Any> BaseViewModel<STATE, SIDE_EFFECT>.collectState(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED
): State<STATE> {
    return this.state.collectAsStateWithLifecycle(minActiveState = lifecycleState)
}

@SuppressLint("ComposableNaming")
@Composable
fun <STATE : Any, SIDE_EFFECT : Any> BaseViewModel<STATE, SIDE_EFFECT>.collectSideEffect(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    sideEffect: (suspend (sideEffect: SIDE_EFFECT) -> Unit)
) {
    val sideEffectFlow = this.sideEffect
    val lifecycleOwner = LocalLifecycleOwner.current

    val callback by rememberUpdatedState(newValue = sideEffect)

    LaunchedEffect(key1 = sideEffectFlow, key2 = lifecycleOwner) {
        sideEffectFlow
            .flowWithLifecycle(lifecycleOwner.lifecycle, lifecycleState)
            .collect { callback(it) }
    }
}