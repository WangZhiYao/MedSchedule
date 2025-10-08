package io.floriax.medschedule.feature.medicationlog.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.floriax.medschedule.core.domain.enums.MedicationState
import io.floriax.medschedule.core.domain.model.TakenMedication
import io.floriax.medschedule.feature.medicationlog.R

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/10/8
 */
@Composable
internal fun StatusIndicator(state: MedicationState) {
    val stateVisuals = getVisualsForState(state)
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(stateVisuals.color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stateVisuals.text,
            style = MaterialTheme.typography.labelLarge,
            color = stateVisuals.color
        )
    }
}

@Composable
internal fun getVisualsForState(state: MedicationState): StateVisuals {
    return when (state) {
        MedicationState.PENDING -> StateVisuals(
            MaterialTheme.colorScheme.tertiary,
            stringResource(R.string.medication_state_pending)
        )

        MedicationState.TAKEN -> StateVisuals(
            MaterialTheme.colorScheme.primary,
            stringResource(R.string.medication_state_taken)
        )

        MedicationState.SKIPPED -> StateVisuals(
            MaterialTheme.colorScheme.onSurfaceVariant,
            stringResource(R.string.medication_state_skipped)
        )

        MedicationState.MISSED -> StateVisuals(
            MaterialTheme.colorScheme.error,
            stringResource(R.string.medication_state_missed)
        )

        else -> StateVisuals(
            MaterialTheme.colorScheme.outline,
            stringResource(R.string.medication_state_unknown)
        )
    }
}

internal data class StateVisuals(val color: Color, val text: String)

@Composable
internal fun TakenMedicationItem(
    takenMedication: TakenMedication,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = takenMedication.medication.name,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
                .basicMarquee(),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "${takenMedication.dose.toPlainString()} ${takenMedication.medication.doseUnit}",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
