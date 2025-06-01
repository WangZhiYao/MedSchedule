package io.floriax.medschedule.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/1
 */
@Entity(
    tableName = "medication_record",
    foreignKeys = [
        ForeignKey(
            entity = MedicationEntity::class,
            parentColumns = ["id"],
            childColumns = ["medication_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class MedicationRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "medication_id", index = true)
    val medicationId: Long,
    @ColumnInfo(name = "medication_time")
    val medicationTime: Long,
    val dose: Float,
    val remark: String,
    val state: Int,
    val type: Int,
    @ColumnInfo(name = "timezone")
    val timeZone: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
)
