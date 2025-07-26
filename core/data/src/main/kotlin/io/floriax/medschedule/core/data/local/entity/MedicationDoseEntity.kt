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
    tableName = "medication_dose",
    foreignKeys = [
        ForeignKey(
            entity = MedicationIntakeEntity::class,
            parentColumns = ["id"],
            childColumns = ["medication_intake_id"],
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
data class MedicationDoseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "medication_intake_id", index = true)
    val medicationIntakeId: Long,
    @ColumnInfo(name = "medication_id", index = true)
    val medicationId: Long,
    val dose: String
)