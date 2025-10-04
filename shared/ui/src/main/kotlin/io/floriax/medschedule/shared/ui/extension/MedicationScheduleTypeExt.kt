package io.floriax.medschedule.shared.ui.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.floriax.medschedule.core.domain.enums.MedicationScheduleType
import io.floriax.medschedule.shared.ui.R

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/27
 */
@Composable
fun MedicationScheduleType.label(): String =
    when (this) {
        MedicationScheduleType.UNKNOWN -> stringResource(R.string.shared_ui_medication_schedule_type_unknown)
        MedicationScheduleType.ONE_TIME -> stringResource(R.string.shared_ui_medication_schedule_type_one_time)
        MedicationScheduleType.DAILY -> stringResource(R.string.shared_ui_medication_schedule_type_daily)
        MedicationScheduleType.WEEKLY -> stringResource(R.string.shared_ui_medication_schedule_type_weekly)
        MedicationScheduleType.INTERVAL -> stringResource(R.string.shared_ui_medication_schedule_type_interval)
        MedicationScheduleType.CUSTOM_CYCLE -> stringResource(R.string.shared_ui_medication_schedule_type_custom_cycle)
    }
