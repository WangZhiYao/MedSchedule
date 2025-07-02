package io.floriax.medschedule.core.domain.enums

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/1
 */
enum class MedicationRecordType(val value: Int) {

    /**
     * 未知
     */
    UNKNOWN(0),

    /**
     * 手动记录
     */
    MANUAL(1),

    /**
     * 自动
     */
    SCHEDULED(2);

    companion object {

        fun fromValue(value: Int): MedicationRecordType =
            entries.find { state -> state.value == value } ?: UNKNOWN

    }

}