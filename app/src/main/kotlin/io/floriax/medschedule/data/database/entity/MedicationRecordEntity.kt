package io.floriax.medschedule.data.database.entity

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
    @ColumnInfo(name = "medication_time")
    val medicationTime: Long,
    val state: Int,
    val type: Int,
    @ColumnInfo(name = "timezone")
    val timeZone: String,
    val remark: String?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
)
