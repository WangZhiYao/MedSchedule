package io.floriax.medschedule.core.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.floriax.medschedule.core.common.di.qualifier.IODispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {

    @Provides
    @Singleton
    @IODispatcher
    fun provideIODispatcher() = Dispatchers.IO

}