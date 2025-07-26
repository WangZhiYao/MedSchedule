package io.floriax.medschedule.core.domain.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.MonthDay

/**
 * 用药时间
 *
 * @author WangZhiYao
 * @since 2025/7/25
 */
sealed class MedicationSchedule {

    /**
     * 一次性计划
     *
     * @property date 执行计划的具体日期
     * @property intakes 当天的服药安排
     */
    data class OneTime(
        val date: LocalDate,
        val intakes: List<MedicationIntake>
    ) : MedicationSchedule()

    /**
     * 重复性计划的基类
     *
     * @property startDate 计划开始日期（包含）
     * @property endDate 计划结束日期（可选，包含）。如果为 null，表示无限期。
     */
    sealed class Repetitive(
        open val startDate: LocalDate,
        open val endDate: LocalDate? = null
    ) : MedicationSchedule() {

        /**
         * 每日重复
         *
         * @property intakes 每天的服药安排（每天都一样）
         */
        sealed class Daily(
            override val startDate: LocalDate,
            override val endDate: LocalDate?,
            val intakes: List<MedicationIntake>
        ) : Repetitive(startDate, endDate)

        /**
         * 每周重复
         *
         * @property daysOfWeek 指定在一周中的哪几天重复，例如 [DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY]
         * @property intakes 在指定日期的服药安排
         */
        sealed class Weekly(
            override val startDate: LocalDate,
            override val endDate: LocalDate?,
            val daysOfWeek: Set<DayOfWeek>,
            val intakes: List<MedicationIntake>
        ) : Repetitive(startDate, endDate)

        /**
         * 每月重复
         *
         * @property daysOfMonth 指定在一个月中的哪几天重复 (1-31)。注意处理大小月和闰年问题。
         * @property intakes 在指定日期的服药安排
         */
        sealed class Monthly(
            override val startDate: LocalDate,
            override val endDate: LocalDate?,
            val daysOfMonth: Set<Int>,
            val intakes: List<MedicationIntake>
        ) : Repetitive(startDate, endDate)

        /**
         * 每年重复
         *
         * @property monthDay 指定在每年的哪一天，例如 MonthDay.of(Month.DECEMBER, 25)
         * @property intakes 在指定日期的服药安排
         */
        data class Annually(
            override val startDate: LocalDate,
            override val endDate: LocalDate?,
            val monthDays: Set<MonthDay>,
            val intakes: List<MedicationIntake>
        ) : Repetitive(startDate, endDate)

        /**
         * 按固定间隔天数重复
         * @property intervalDays 间隔天数，例如 2 表示 "每隔2天" (即每3天一次)
         * @property intakes 在指定日期的服药安排
         */
        data class Interval(
            override val startDate: LocalDate,
            override val endDate: LocalDate?,
            val intervalDays: Int,
            val intakes: List<MedicationIntake>
        ) : Repetitive(startDate, endDate)

        /**
         * 自定义周期计划
         * @property cycleLengthInDays 整个周期的总天数
         * @property cycleDays 定义了周期内每一天的具体用药情况
         */
        data class CustomCycle(
            override val startDate: LocalDate,
            override val endDate: LocalDate?,
            val cycleLengthInDays: Int,
            val cycleDays: List<CustomCycleDay>
        ) : Repetitive(startDate, endDate) {

            data class CustomCycleDay(
                val dayOfCycle: Int,
                val intakes: List<MedicationIntake>
            )
        }
    }
}
