package io.floriax.medschedule.navigation

import kotlinx.serialization.Serializable

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
sealed class Route {

    @Serializable
    data object Home : Route()

    @Serializable
    data object MedicationList : Route()

    @Serializable
    data object AddMedication : Route()

    @Serializable
    data class EditMedication(val id: Long) : Route()

    @Serializable
    data object AddMedicationRecord : Route()

}