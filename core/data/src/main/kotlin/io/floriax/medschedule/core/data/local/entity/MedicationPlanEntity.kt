package io.floriax.medschedule.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/26
 */
@Entity(tableName = "medication_plan")
data class MedicationPlanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val notes: String?,
    val active: Boolean,
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)