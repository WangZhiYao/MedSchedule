package io.floriax.medschedule.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.floriax.medschedule.core.data.repository.MedicationRecordRepositoryImpl
import io.floriax.medschedule.core.data.repository.MedicationRepositoryImpl
import io.floriax.medschedule.core.domain.repository.MedicationRecordRepository
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
    abstract fun bindMedicationRecordRepository(impl: MedicationRecordRepositoryImpl): MedicationRecordRepository

}