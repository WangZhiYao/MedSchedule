package io.floriax.medschedule.core.common.logging

import timber.log.Timber

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/19
 */
class Logger private constructor(val formattedTag: String) {

    fun v(message: String?, vararg args: Any?) {
        Timber.tag(formattedTag).v(message, *args)
    }

    fun v(t: Throwable?, message: String?, vararg args: Any?) {
        Timber.tag(formattedTag).v(t, message, *args)
    }

    fun v(t: Throwable?) {
        Timber.tag(formattedTag).v(t)
    }

    fun d(message: String?, vararg args: Any?) {
        Timber.tag(formattedTag).d(message, *args)
    }

    fun d(t: Throwable?, message: String?, vararg args: Any?) {
        Timber.tag(formattedTag).d(t, message, *args)
    }

    fun d(t: Throwable?) {
        Timber.tag(formattedTag).d(t)
    }

    fun i(message: String?, vararg args: Any?) {
        Timber.tag(formattedTag).i(message, *args)
    }

    fun i(t: Throwable?, message: String?, vararg args: Any?) {
        Timber.tag(formattedTag).i(t, message, *args)
    }

    fun i(t: Throwable?) {
        Timber.tag(formattedTag).i(t)
    }

    fun w(message: String?, vararg args: Any?) {
        Timber.tag(formattedTag).w(message, *args)
    }

    fun w(t: Throwable?, message: String?, vararg args: Any?) {
        Timber.tag(formattedTag).w(t, message, *args)
    }

    fun w(t: Throwable?) {
        Timber.tag(formattedTag).w(t)
    }

    fun e(message: String?, vararg args: Any?) {
        Timber.tag(formattedTag).e(message, *args)
    }

    fun e(t: Throwable?, message: String?, vararg args: Any?) {
        Timber.tag(formattedTag).e(t, message, *args)
    }

    fun e(t: Throwable?) {
        Timber.tag(formattedTag).e(t)
    }

    fun wtf(message: String?, vararg args: Any?) {
        Timber.tag(formattedTag).wtf(message, *args)
    }

    fun wtf(t: Throwable?, message: String?, vararg args: Any?) {
        Timber.tag(formattedTag).wtf(t, message, *args)
    }

    fun wtf(t: Throwable?) {
        Timber.tag(formattedTag).wtf(t)
    }

    companion object {

        private const val TAG_PREFIX = "Meds"

        fun init(minLogLevel: LogLevel) {
            Timber.plant(LevelFilteredDebugTree(minLogLevel))
        }

        fun create(tag: String) = Logger(buildTag(tag))

        fun v(tag: String, message: String?, vararg args: Any?) {
            Timber.tag(buildTag(tag)).v(message, *args)
        }

        fun v(tag: String, t: Throwable?, message: String?, vararg args: Any?) {
            Timber.tag(buildTag(tag)).v(t, message, *args)
        }

        fun v(tag: String, t: Throwable?) {
            Timber.tag(buildTag(tag)).v(t)
        }

        fun d(tag: String, message: String?, vararg args: Any?) {
            Timber.tag(buildTag(tag)).d(message, *args)
        }

        fun d(tag: String, t: Throwable?, message: String?, vararg args: Any?) {
            Timber.tag(buildTag(tag)).d(t, message, *args)
        }

        fun d(tag: String, t: Throwable?) {
            Timber.tag(buildTag(tag)).d(t)
        }

        fun i(tag: String, message: String?, vararg args: Any?) {
            Timber.tag(buildTag(tag)).i(message, *args)
        }

        fun i(tag: String, t: Throwable?, message: String?, vararg args: Any?) {
            Timber.tag(buildTag(tag)).i(t, message, *args)
        }

        fun i(tag: String, t: Throwable?) {
            Timber.tag(buildTag(tag)).i(t)
        }

        fun w(tag: String, message: String?, vararg args: Any?) {
            Timber.tag(buildTag(tag)).w(message, *args)
        }

        fun w(tag: String, t: Throwable?, message: String?, vararg args: Any?) {
            Timber.tag(buildTag(tag)).w(t, message, *args)
        }

        fun w(tag: String, t: Throwable?) {
            Timber.tag(buildTag(tag)).w(t)
        }

        fun e(tag: String, message: String?, vararg args: Any?) {
            Timber.tag(buildTag(tag)).e(message, *args)
        }

        fun e(tag: String, t: Throwable?, message: String?, vararg args: Any?) {
            Timber.tag(buildTag(tag)).e(t, message, *args)
        }

        fun e(tag: String, t: Throwable?) {
            Timber.tag(buildTag(tag)).e(t)
        }

        fun wtf(tag: String, message: String?, vararg args: Any?) {
            Timber.tag(buildTag(tag)).wtf(message, *args)
        }

        fun wtf(tag: String, t: Throwable?, message: String?, vararg args: Any?) {
            Timber.tag(buildTag(tag)).wtf(t, message, *args)
        }

        fun wtf(tag: String, t: Throwable?) {
            Timber.tag(buildTag(tag)).wtf(t)
        }

        private fun buildTag(tag: String): String = "$TAG_PREFIX:$tag"

    }
}