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
    tableName = "medication_record_entry",
    primaryKeys = ["medication_record_id", "medication_id"],
    foreignKeys = [
        ForeignKey(
            entity = MedicationRecordEntity::class,
            parentColumns = ["id"],
            childColumns = ["medication_record_id"],
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
data class MedicationRecordEntryEntity(
    @ColumnInfo(name = "medication_record_id", index = true)
    val medicationRecordId: Long,
    @ColumnInfo(name = "medication_id", index = true)
    val medicationId: Long,
    val dose: String,
    @ColumnInfo(name = "deduct_from_stock")
    val deductFromStock: Boolean,
)