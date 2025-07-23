package io.floriax.medschedule.core.data.di

import android.content.Context
import androidx.paging.PagingConfig
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.floriax.medschedule.core.common.di.qualifier.IODispatcher
import io.floriax.medschedule.core.common.extension.logger
import io.floriax.medschedule.core.data.local.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DATABASE_NAME = "med_schedule.db"

    private val logger by logger<DatabaseModule>()

    @Provides
    @Singleton
    fun provideDatabasePagingConfig(): PagingConfig =
        PagingConfig(
            pageSize = 20,
            prefetchDistance = 5,
            enablePlaceholders = false
        )

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext applicationContext: Context,
        @IODispatcher ioDispatcher: CoroutineDispatcher
    ): AppDatabase =
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, DATABASE_NAME)
            .setQueryCoroutineContext(ioDispatcher)
            .setQueryCallback(ioDispatcher) { query, bindArgs ->
                logger.d("SQL: $query  args: $bindArgs")
            }
            .build()

    @Provides
    @Singleton
    fun provideMedicationDao(appDatabase: AppDatabase) = appDatabase.medicationDao()

    @Provides
    @Singleton
    fun provideMedicationLogDao(appDatabase: AppDatabase) = appDatabase.medicationLogDao()

    @Provides
    @Singleton
    fun provideMedicationLogEntryDao(appDatabase: AppDatabase) =
        appDatabase.medicationLogEntryDao()

}