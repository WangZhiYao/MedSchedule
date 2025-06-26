package io.floriax.medschedule

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.floriax.medschedule.core.common.logging.LogLevel
import io.floriax.medschedule.core.common.logging.Logger

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
@HiltAndroidApp
class MedScheduleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Logger.init(LOG_LEVEL)
    }

    companion object {
        val LOG_LEVEL: LogLevel = if (BuildConfig.DEBUG) LogLevel.DEBUG else LogLevel.WARN
    }
}