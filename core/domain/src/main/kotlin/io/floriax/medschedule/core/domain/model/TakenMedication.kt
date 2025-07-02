package io.floriax.medschedule.core.domain.model

import java.math.BigDecimal

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/1
 */
data class TakenMedication(
    val medication: Medication,
    val dose: BigDecimal
)