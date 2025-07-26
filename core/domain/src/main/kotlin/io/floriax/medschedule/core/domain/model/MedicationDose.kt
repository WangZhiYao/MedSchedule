package io.floriax.medschedule.core.domain.model

import java.math.BigDecimal

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/25
 */
data class MedicationDose(
    val medication: Medication,
    val dose: BigDecimal
)