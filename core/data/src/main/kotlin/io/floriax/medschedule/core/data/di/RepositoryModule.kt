package io.floriax.medschedule.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.floriax.medschedule.core.data.repository.MedicationLogRepositoryImpl
import io.floriax.medschedule.core.data.repository.MedicationRepositoryImpl
import io.floriax.medschedule.core.domain.repository.MedicationLogRepository
import io.floriax.medschedule.core.domain.repository.MedicationRepository
import javax.inject.Singleton

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/27
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMedicationRepository(impl: MedicationRepositoryImpl): MedicationRepository

    @Binds
    @Singleton
    abstract fun bindMedicationLogRepository(impl: MedicationLogRepositoryImpl): MedicationLogRepository

}