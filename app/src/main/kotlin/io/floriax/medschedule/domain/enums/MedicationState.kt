package io.floriax.medschedule.domain.enums

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/1
 */
enum class MedicationState(val value: Int) {

    /**
     * 未知
     */
    UNKNOWN(0),

    /**
     * 已计划但还未执行
     */
    SCHEDULED(1),

    /**
     * 已服药
     */
    TAKEN(2),

    /**
     * 用户主动跳过
     */
    SKIPPED(3),

    /**
     * 错过未服
     */
    MISSED(4),

    /**
     * 被取消（例如提醒已删除）
     */
    CANCELED(5);

    companion object {

        fun fromValue(value: Int): MedicationState =
            entries.find { state -> state.value == value } ?: UNKNOWN

    }

}