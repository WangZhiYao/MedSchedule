package io.floriax.medschedule.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
@Entity(tableName = "medication")
data class MedicationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    @ColumnInfo(name = "dose_unit")
    val doseUnit: String,
    val notes: String?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)