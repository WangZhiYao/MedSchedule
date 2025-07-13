package io.floriax.medschedule.shared.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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

@Composable
fun EditButton(
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = AppIcons.Edit,
            contentDescription = stringResource(R.string.shared_ui_edit)
        )
    }
}

@Composable
fun DeleteButton(
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = AppIcons.Delete,
            contentDescription = stringResource(R.string.shared_ui_delete),
            tint = MaterialTheme.colorScheme.error
        )
    }
}