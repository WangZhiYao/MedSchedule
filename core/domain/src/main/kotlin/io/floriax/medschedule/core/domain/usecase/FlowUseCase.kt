package io.floriax.medschedule.core.domain.usecase

import kotlinx.coroutines.flow.Flow

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/15
 */
interface FlowUseCase<in P, out R> {

    operator fun invoke(params: P): Flow<R>

}

operator fun <R> FlowUseCase<Unit, R>.invoke(): Flow<R> = this.invoke(Unit)
