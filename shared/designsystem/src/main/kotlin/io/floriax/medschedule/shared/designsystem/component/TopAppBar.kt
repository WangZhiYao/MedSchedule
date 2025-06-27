package io.floriax.medschedule.shared.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/6/27
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedScheduleTopAppBar(
    @StringRes titleRes: Int,
    @StringRes subtitleRes: Int,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = stringResource(titleRes),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(subtitleRes),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun MedScheduleTopAppBarPreview() {
    MedScheduleTopAppBar(
        titleRes = android.R.string.untitled,
        subtitleRes = android.R.string.untitled
    )
}