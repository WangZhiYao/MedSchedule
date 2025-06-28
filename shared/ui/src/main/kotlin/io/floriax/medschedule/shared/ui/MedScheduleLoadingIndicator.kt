package io.floriax.medschedule.shared.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.floriax.medschedule.shared.designsystem.theme.AppTheme

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/28
 */
@Composable
fun MedScheduleLoadingIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
fun MedScheduleLoadingIndicatorPreview() {
    AppTheme {
        MedScheduleLoadingIndicator()
    }
}