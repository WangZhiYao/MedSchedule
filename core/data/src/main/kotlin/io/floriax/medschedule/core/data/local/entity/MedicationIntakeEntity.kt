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
    tableName = "medication_intake",
    foreignKeys = [
        ForeignKey(
            entity = MedicationScheduleEntity::class,
            parentColumns = ["id"],
            childColumns = ["medication_schedule_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MedicationIntakeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "medication_schedule_id", index = true)
    val medicationScheduleId: Long,
    val time: Long,
    // 对于 CustomCycle，需要标识这是哪一天的 intake
    val cycleDay: Int?
)