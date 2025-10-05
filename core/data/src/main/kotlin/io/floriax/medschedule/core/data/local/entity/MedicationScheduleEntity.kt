package io.floriax.medschedule.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/26
 */
@Entity(
    tableName = "medication_schedule",
    foreignKeys = [
        ForeignKey(
            entity = MedicationPlanEntity::class,
            parentColumns = ["id"],
            childColumns = ["medication_plan_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MedicationScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo("medication_plan_id", index = true)
    val medicationPlanId: Long,

    /**
     * 时间安排类型标识符
     *
     * 使用整数枚举标识不同的时间安排类型
     *
     * @see io.floriax.medschedule.core.domain.enums.MedicationScheduleType
     *
     */
    @ColumnInfo(name = "schedule_type")
    val scheduleType: Int,

    /**
     * 计划开始日期
     *
     * 以 ISO-8601 格式存储的日期字符串 (yyyy-MM-dd)。
     * 适用于所有重复性计划类型 (DAILY, WEEKLY, MONTHLY, ANNUALLY, INTERVAL, CUSTOM_CYCLE)。
     * 表示计划生效的起始日期（包含该日期）。
     * 对于 ONE_TIME 类型，此字段为 null。
     *
     * @sample "2024-01-15"
     */
    @ColumnInfo(name = "start_date")
    val startDate: String?,

    /**
     * 计划结束日期
     *
     * 以 ISO-8601 格式存储的日期字符串 (yyyy-MM-dd)。
     * 适用于所有重复性计划类型，表示计划的最后有效日期（包含该日期）。
     * 如果为 null，表示无限期执行直到用户手动停止。
     * 对于 ONE_TIME 类型，此字段为 null。
     *
     * @sample "2024-12-31"
     */
    @ColumnInfo(name = "end_date")
    val endDate: String?,

    /**
     * 一次性计划的执行日期
     *
     * 以 ISO-8601 格式存储的日期字符串 (yyyy-MM-dd)。
     * 仅用于 scheduleType = 1 (ONE_TIME) 的情况。
     * 表示一次性用药计划的具体执行日期。
     * 对于其他所有计划类型，此字段为 null。
     *
     * @sample "2024-03-20"
     */
    @ColumnInfo(name = "one_time_schedule_date")
    val oneTimeScheduleDate: String?,

    /**
     * 每周重复计划的指定星期几
     *
     * 以 JSON 数组格式存储的整数列表，表示一周中的哪几天需要用药。
     * 仅用于 scheduleType = 3 (WEEKLY) 的情况。
     * 数值对应 java.time.DayOfWeek 的 value 属性：
     * - 1: 星期一 (MONDAY)
     * - 2: 星期二 (TUESDAY)
     * - 3: 星期三 (WEDNESDAY)
     * - 4: 星期四 (THURSDAY)
     * - 5: 星期五 (FRIDAY)
     * - 6: 星期六 (SATURDAY)
     * - 7: 星期日 (SUNDAY)
     *
     * @sample "[1,3,5]" 表示每周一、三、五用药
     * @sample "[6,7]" 表示每个周末用药
     */
    @ColumnInfo(name = "days_of_week")
    val daysOfWeek: String?,

    /**
     * 每月重复计划的指定日期
     *
     * 以 JSON 数组格式存储的整数列表，表示每月中的哪几天需要用药。
     * 仅用于 scheduleType = 4 (MONTHLY) 的情况。
     * 数值范围为 1-31，表示月份中的具体日期。
     *
     * 注意事项：
     * - 对于小月（30天）和二月，如果指定了不存在的日期（如31号），会自动跳过
     * - 建议在业务逻辑中处理闰年的情况
     *
     * @sample "[1,15]" 表示每月1号和15号用药
     * @sample "[28,29,30,31]" 表示每月月末几天用药（具体天数根据月份调整）
     */
    @ColumnInfo(name = "days_of_month")
    val daysOfMonth: String?,

    /**
     * 每年重复计划的指定日期
     *
     * 以 JSON 数组格式存储的日期字符串列表，表示每年中的哪些特定日期需要用药。
     * 仅用于 scheduleType = 5 (ANNUALLY) 的情况。
     * 每个字符串使用 "MM-dd" 格式表示月份和日期。
     *
     * 注意事项：
     * - 对于闰年的2月29日，在非闰年会自动跳过或调整至2月28日
     * - 支持多个年度日期，适用于生日、纪念日等复杂场景
     *
     * @sample "[\"12-25\",\"01-01\"]" 表示每年圣诞节和元旦用药
     * @sample "[\"02-29\",\"06-15\",\"10-01\"]" 表示每年闰年2月29日、6月15日和10月1日用药
     */
    @ColumnInfo(name = "annual_month_days")
    val annualMonthDays: String?,

    /**
     * 间隔重复计划的间隔天数
     *
     * 正整数，表示按固定间隔天数重复用药的间隔值。
     * 仅用于 scheduleType = 6 (INTERVAL) 的情况。
     * 表示从 startDate 开始，每隔指定天数重复用药。
     *
     * 计算规则：
     * - intervalDays = 1: 每天用药（相当于 DAILY）
     * - intervalDays = 2: 每隔2天用药一次（即每3天用药一次）
     * - intervalDays = 7: 每隔7天用药一次（即每周用药一次）
     *
     * @sample 2 表示"每隔2天用药一次"，实际用药频率为每3天
     * @sample 7 表示"每隔7天用药一次"，实际用药频率为每8天
     */
    @ColumnInfo(name = "interval_days")
    val intervalDays: Int?,

    /**
     * 自定义周期计划的周期长度
     *
     * 正整数，表示自定义周期的总天数。
     * 仅用于 scheduleType = 7 (CUSTOM_CYCLE) 的情况。
     * 配合 CustomCycleDay 数据来定义复杂的周期性用药安排。
     *
     * 自定义周期允许用户创建复杂的重复模式，例如：
     * - 21天为一个周期，前5天每天用药，后16天不用药
     * - 28天为一个周期，模拟月经周期用药
     * - 14天为一个周期，前7天用药A，后7天用药B
     *
     * 周期会从 startDate 开始，按照定义的模式重复执行直到 endDate。
     *
     * @sample 21 表示21天为一个完整周期
     * @sample 28 表示28天为一个完整周期
     */
    @ColumnInfo(name = "cycle_length_in_days")
    val cycleLengthInDays: Int?
)