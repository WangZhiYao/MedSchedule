package io.floriax.medschedule.shared.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/10/4
 */
@Composable
fun WeekDaySelector(
    selectedDays: Set<DayOfWeek>,
    onDayClick: (DayOfWeek) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    locale: Locale = Locale.getDefault(),
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DayOfWeek.entries.forEach { day ->
            val selected = day in selectedDays
            FilterChip(
                selected = selected,
                onClick = { onDayClick(day) },
                label = {
                    Text(
                        text = day.getDisplayName(TextStyle.NARROW, locale),
                        color = if (isError) MaterialTheme.colorScheme.error else Color.Unspecified
                    )
                },
                border = if (isError) {
                    FilterChipDefaults.filterChipBorder(
                        selected = selected,
                        enabled = true,
                        borderColor = MaterialTheme.colorScheme.error
                    )
                } else {
                    FilterChipDefaults.filterChipBorder(true, selected)
                }
            )
        }
    }
}