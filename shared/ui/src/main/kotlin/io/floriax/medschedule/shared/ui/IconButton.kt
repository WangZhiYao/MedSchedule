package io.floriax.medschedule.shared.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.floriax.medschedule.shared.designsystem.icon.AppIcons

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/12
 */
@Composable
fun BackButton(
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = AppIcons.ArrowBack,
            contentDescription = stringResource(R.string.shared_ui_back)
        )
    }
}