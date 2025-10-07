package io.floriax.medschedule.core.domain.model.ext

import io.floriax.medschedule.core.domain.model.MedicationIntake
import io.floriax.medschedule.core.domain.model.MedicationSchedule
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

/**
 * 计算给定时间点之后的下一次用药时间。
 *
 * 此函数通过 [generatePotentialDates] 生成未来可能的日期序列，
 * 然后在这些日期中查找在 [after] 参数之后的最早的 [LocalDateTime]。
 *
 * @param after 用于开始搜索的起始时间点。默认为 `Instant.now()`。
 * @return 如果计划配置不正确或没有未来的执行时间，则返回 `null`，否则返回下一次执行的 [LocalDateTime]。
 */
fun MedicationSchedule.nextOccurrence(after: Instant = Instant.now()): LocalDateTime? {
    if (!hasOccurrencesConfigured()) return null

    val zoneId = ZoneId.systemDefault()
    val afterDateTime = after.atZone(zoneId).toLocalDateTime()
    val afterDate = afterDateTime.toLocalDate()
    val afterTime = afterDateTime.toLocalTime()

    for (date in generatePotentialDates(afterDate)) {
        val intakesForDate = getIntakesForDate(date)
        if (intakesForDate.isEmpty()) continue

        val nextTime = if (date == afterDate) {
            intakesForDate.asSequence()
                .map { it.time }
                .filter { it.isAfter(afterTime) }
                .minOrNull()
        } else {
            intakesForDate.minOfOrNull { it.time }
        }

        if (nextTime != null) {
            return LocalDateTime.of(date, nextTime)
        }
    }

    return null
}

/**
 * 计算在给定时间点或之前的最近一次用药时间。
 *
 * @param before 用于开始反向搜索的截止时间点。默认为 `Instant.now()`。
 * @return 如果计划还未开始或配置不正确，则返回 `null`，否则返回最近一次执行的 [LocalDateTime]。
 */
fun MedicationSchedule.previousOrCurrentOccurrence(before: Instant = Instant.now()): LocalDateTime? {
    if (!hasOccurrencesConfigured()) return null

    val zoneId = ZoneId.systemDefault()
    val beforeDateTime = before.atZone(zoneId).toLocalDateTime()
    val beforeDate = beforeDateTime.toLocalDate()
    val beforeTime = beforeDateTime.toLocalTime()

    for (date in generatePotentialDatesReversed(beforeDate)) {
        val intakesForDate = getIntakesForDate(date)
        if (intakesForDate.isEmpty()) continue

        val previousTime = if (date == beforeDate) {
            intakesForDate.asSequence()
                .map { it.time }
                .filter { !it.isAfter(beforeTime) }
                .maxOrNull()
        } else {
            intakesForDate.maxOfOrNull { it.time }
        }

        if (previousTime != null) {
            return LocalDateTime.of(date, previousTime)
        }
    }

    return null
}

/**
 * 获取在指定日期的所有计划执行时间。
 *
 * @param date 要获取执行时间的日期。
 * @return 一个按时间顺序排序的 [LocalDateTime] 列表，代表当天的所有执行时间。
 */
fun MedicationSchedule.getOccurrencesOn(date: LocalDate): List<LocalDateTime> {
    // getIntakesForDate 内部已经检查了日期模式是否匹配。
    val intakes = getIntakesForDate(date)
    if (intakes.isEmpty()) return emptyList()

    // 检查日期本身是否在计划的有效范围内（例如，在开始和结束日期之间）。
    val isValidDate = when (this) {
        is MedicationSchedule.OneTime -> date == this.date
        is MedicationSchedule.Repetitive -> {
            val afterStart = !date.isBefore(this.startDate)
            val beforeEnd = this.endDate == null || !date.isAfter(this.endDate)
            afterStart && beforeEnd
        }
    }

    if (!isValidDate) return emptyList()

    return intakes.map { LocalDateTime.of(date, it.time) }.sorted()
}

/**
 * 生成一个从指定日期开始的、符合计划规则的潜在未来日期序列。
 *
 * @param after 用于开始生成日期的起始日期。
 * @return 一个懒加载的 [LocalDate] 序列。
 */
private fun MedicationSchedule.generatePotentialDates(after: LocalDate): Sequence<LocalDate> =
    when (this) {
        is MedicationSchedule.OneTime ->
            if (!hasOccurrencesConfigured() || date.isBefore(after)) {
                emptySequence()
            } else {
                sequenceOf(date)
            }

        is MedicationSchedule.Repetitive ->
            if (!hasOccurrencesConfigured()) {
                emptySequence()
            } else {
                val searchStart = if (after.isAfter(startDate)) after else startDate
                val end = endDate

                if (end != null && searchStart.isAfter(end)) {
                    emptySequence()
                } else {
                    generateSequence(searchStart) { current ->
                        val next = current.plusDays(1)
                        if (end != null && next.isAfter(end)) null else next
                    }.filter { date -> isDatePatternActive(date) }
                }
            }
    }

/**
 * 生成一个从指定日期开始的、符合计划规则的潜在过去日期序列（反向）。
 *
 * @param before 用于开始反向生成日期的截止日期。
 * @return 一个懒加载的、反向的 [LocalDate] 序列。
 */
private fun MedicationSchedule.generatePotentialDatesReversed(before: LocalDate): Sequence<LocalDate> =
    when (this) {
        is MedicationSchedule.OneTime ->
            if (!hasOccurrencesConfigured() || date.isAfter(before)) {
                emptySequence()
            } else {
                sequenceOf(date)
            }

        is MedicationSchedule.Repetitive ->
            if (!hasOccurrencesConfigured()) {
                emptySequence()
            } else {
                val end = endDate
                val searchStart = if (end != null && end.isBefore(before)) end else before

                if (searchStart.isBefore(startDate)) {
                    emptySequence()
                } else {
                    generateSequence(searchStart) { current ->
                        val previous = current.minusDays(1)
                        if (previous.isBefore(startDate)) null else previous
                    }.filter { date -> isDatePatternActive(date) }
                }
            }
    }

/**
 * 检查给定日期是否符合计划的重复模式（例如，星期几、间隔天数）。
 * 注意：此函数不检查日期是否在计划的开始/结束日期范围内。
 *
 * @param date 要检查的日期。
 * @return 如果日期符合重复模式，则为 `true`。
 */
private fun MedicationSchedule.isDatePatternActive(date: LocalDate): Boolean =
    when (this) {
        is MedicationSchedule.OneTime -> date == this.date
        is MedicationSchedule.Repetitive.Daily -> true
        is MedicationSchedule.Repetitive.Weekly -> date.dayOfWeek in this.daysOfWeek
        is MedicationSchedule.Repetitive.Interval ->
            ChronoUnit.DAYS.between(this.startDate, date) % (this.intervalDays + 1) == 0L

        is MedicationSchedule.Repetitive.CustomCycle -> {
            val dayInCycle =
                (ChronoUnit.DAYS.between(this.startDate, date) % this.cycleLengthInDays + 1).toInt()
            this.cycleDays.any { it.dayOfCycle == dayInCycle }
        }
    }

/**
 * 获取在指定日期的所有用药时间安排（Intakes）。
 * 此函数仅在日期符合重复模式时才返回结果。
 *
 * @param date 要获取用药安排的日期。
 * @return 一个 [MedicationIntake] 列表。
 */
private fun MedicationSchedule.getIntakesForDate(date: LocalDate): List<MedicationIntake> {
    if (!this.isDatePatternActive(date)) return emptyList()

    return when (this) {
        is MedicationSchedule.OneTime -> this.intakes
        is MedicationSchedule.Repetitive.Daily -> this.intakes
        is MedicationSchedule.Repetitive.Weekly -> this.intakes
        is MedicationSchedule.Repetitive.Interval -> this.intakes
        is MedicationSchedule.Repetitive.CustomCycle -> {
            val dayInCycle =
                (ChronoUnit.DAYS.between(this.startDate, date) % this.cycleLengthInDays + 1).toInt()
            this.cycleDays.find { it.dayOfCycle == dayInCycle }?.intakes ?: emptyList()
        }
    }
}

/**
 * 检查计划是否被正确配置，以至于可能产生任何执行时间。
 *
 * 例如，一个没有选择任何星期几的每周计划，或是一个没有设置用药时间的每日计划，
 * 都被认为是未正确配置的。此函数有助于提前终止对无效计划的计算。
 *
 * @return 如果计划有效且定义了用药时间，则为 `true`。
 */
private fun MedicationSchedule.hasOccurrencesConfigured(): Boolean =
    when (this) {
        is MedicationSchedule.OneTime -> intakes.isNotEmpty()
        is MedicationSchedule.Repetitive.Daily -> intakes.isNotEmpty()
        is MedicationSchedule.Repetitive.Weekly -> intakes.isNotEmpty() && daysOfWeek.isNotEmpty()
        is MedicationSchedule.Repetitive.Interval -> intakes.isNotEmpty() && intervalDays >= 0
        is MedicationSchedule.Repetitive.CustomCycle -> cycleLengthInDays > 0
                && cycleDays.any { day -> day.dayOfCycle in 1..cycleLengthInDays && day.intakes.isNotEmpty() }
    }
