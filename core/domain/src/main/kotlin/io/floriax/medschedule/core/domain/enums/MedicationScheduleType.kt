package io.floriax.medschedule.core.domain.enums

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/26
 */
enum class MedicationScheduleType(val value: Int) {

    UNKNOWN(0),

    ONE_TIME(1),

    DAILY(2),

    WEEKLY(3),

    INTERVAL(4),

    CUSTOM_CYCLE(5)

}