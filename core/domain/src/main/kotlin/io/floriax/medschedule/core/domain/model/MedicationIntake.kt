package io.floriax.medschedule.core.domain.model

import java.time.LocalTime

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/25
 */
data class MedicationIntake(
    val time: LocalTime,
    val medicationDoses: List<MedicationDose>
)