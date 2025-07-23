package io.floriax.medschedule.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/1
 */
@Entity(
    tableName = "medication_log_entry",
    primaryKeys = ["medication_log_id", "medication_id"],
    foreignKeys = [
        ForeignKey(
            entity = MedicationLogEntity::class,
            parentColumns = ["id"],
            childColumns = ["medication_log_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MedicationEntity::class,
            parentColumns = ["id"],
            childColumns = ["medication_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MedicationLogEntryEntity(
    @ColumnInfo(name = "medication_log_id", index = true)
    val medicationLogId: Long,
    @ColumnInfo(name = "medication_id", index = true)
    val medicationId: Long,
    val dose: String,
    @ColumnInfo(name = "deduct_from_stock")
    val deductFromStock: Boolean,
)