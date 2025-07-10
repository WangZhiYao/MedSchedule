package io.floriax.medschedule.core.common.exception

/**
 * 当药品库存不足以完成一次用药记录时抛出。
 * @param medicationName 库存不足的药品名称。
 *
 * @author WangZhiYao
 * @since 2025/7/10
 */
class InsufficientStockException(val medicationName: String) :
    RuntimeException("Insufficient stock for $medicationName")