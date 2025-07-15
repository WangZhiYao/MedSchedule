package io.floriax.medschedule.core.domain.usecase

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/15
 */
interface SuspendUseCase<in P, out R> {

    suspend operator fun invoke(params: P): R

}

suspend operator fun <R> SuspendUseCase<Unit, R>.invoke(): R = this.invoke(Unit)
