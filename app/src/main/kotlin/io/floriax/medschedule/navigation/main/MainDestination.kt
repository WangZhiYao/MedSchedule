package io.floriax.medschedule.navigation.main

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import io.floriax.medschedule.core.ui.icon.AppIcons
import io.floriax.medschedule.feature.home.navigation.HomeRoute
import io.floriax.medschedule.feature.medication.navigation.MedicineCabinetRoute
import io.floriax.medschedule.feature.medicationplan.navigation.MedicationPlanRoute
import io.floriax.medschedule.feature.medicationrecord.navigation.MedicationRecordRoute
import kotlin.reflect.KClass
import io.floriax.medschedule.feature.home.R as homeR
import io.floriax.medschedule.feature.medication.R as medicationR
import io.floriax.medschedule.feature.medicationplan.R as medicationPlanR
import io.floriax.medschedule.feature.medicationrecord.R as medicationRecordR

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

    MEDICATION_PLAN(
        titleRes = medicationPlanR.string.feature_medication_plan_title,
        route = MedicationPlanRoute::class
    ),

    MEDICATION(
        titleRes = medicationR.string.feature_medication_title,
        route = MedicineCabinetRoute::class
    ),

    MEDICATION_RECORD(
        titleRes = medicationRecordR.string.feature_medication_record_title,
        route = MedicationRecordRoute::class
    );

    val selectedIcon: ImageVector
        @Composable get() = when (this) {
            HOME -> AppIcons.Home
            MEDICATION_PLAN -> AppIcons.CalendarClock
            MEDICATION -> AppIcons.MedicalServices
            MEDICATION_RECORD -> AppIcons.History
        }

    val unselectedIcon: ImageVector
        @Composable get() = when (this) {
            HOME -> AppIcons.HomeBorder
            MEDICATION_PLAN -> AppIcons.CalendarClockBorder
            MEDICATION -> AppIcons.MedicalServicesBorder
            MEDICATION_RECORD -> AppIcons.HistoryBorder
        }
}