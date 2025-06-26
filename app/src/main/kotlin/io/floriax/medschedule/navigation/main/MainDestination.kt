package io.floriax.medschedule.navigation.main

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import io.floriax.medschedule.core.ui.icon.AppIcon
import io.floriax.medschedule.feature.home.navigation.HomeRoute
import io.floriax.medschedule.feature.medication.navigation.MedicineCabinetRoute
import io.floriax.medschedule.feature.record.navigation.MedicationRecordRoute
import io.floriax.medschedule.feature.schedule.navigation.MedicationScheduleRoute
import kotlin.reflect.KClass
import io.floriax.medschedule.feature.home.R as homeR
import io.floriax.medschedule.feature.medication.R as medicationR
import io.floriax.medschedule.feature.record.R as recordR
import io.floriax.medschedule.feature.schedule.R as scheduleR


/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/26
 */
enum class MainDestination(
    @param:StringRes val titleRes: Int,
    val route: KClass<*>
) {

    HOME(
        titleRes = homeR.string.feature_home_title,
        route = HomeRoute::class
    ),

    MEDICATION_SCHEDULE(
        titleRes = scheduleR.string.feature_medication_schedule_title,
        route = MedicationScheduleRoute::class
    ),

    MEDICINE_CABINET(
        titleRes = medicationR.string.feature_medicine_cabinet_title,
        route = MedicineCabinetRoute::class
    ),

    MEDICATION_RECORD(
        titleRes = recordR.string.feature_medication_record_title,
        route = MedicationRecordRoute::class
    );

    val selectedIcon: ImageVector
        @Composable get() = when (this) {
            HOME -> AppIcon.Home
            MEDICATION_SCHEDULE -> AppIcon.CalendarClock
            MEDICINE_CABINET -> AppIcon.MedicalServices
            MEDICATION_RECORD -> AppIcon.History
        }

    val unselectedIcon: ImageVector
        @Composable get() = when (this) {
            HOME -> AppIcon.HomeBorder
            MEDICATION_SCHEDULE -> AppIcon.CalendarClockBorder
            MEDICINE_CABINET -> AppIcon.MedicalServicesBorder
            MEDICATION_RECORD -> AppIcon.HistoryBorder
        }
}