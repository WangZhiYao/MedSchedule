package io.floriax.medschedule.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/1
 */
@Entity(tableName = "medication_record")
data class MedicationRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "medication_time", index = true)
    val medicationTime: Long,
    val state: Int,
    val type: Int,
    @ColumnInfo(name = "time_zone")
    val timeZone: String,
    val notes: String?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
)