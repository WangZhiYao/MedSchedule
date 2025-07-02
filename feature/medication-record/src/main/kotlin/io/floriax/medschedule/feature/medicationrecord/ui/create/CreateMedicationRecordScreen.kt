package io.floriax.medschedule.feature.medicationrecord.ui.create

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.floriax.medschedule.feature.medicationrecord.R
import io.floriax.medschedule.shared.designsystem.icon.AppIcons
import io.floriax.medschedule.shared.designsystem.theme.AppTheme
import io.floriax.medschedule.shared.ui.R as sharedUiR

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/7/2
 */
@Composable
fun CreateMedicationRecordRoute(
    onBackClick: () -> Unit
) {
    CreateMedicationRecordScreen(
        onBackClick = onBackClick
    )
}

@Composable
private fun CreateMedicationRecordScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CreateMedicationRecordTopBar(
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateMedicationRecordTopBar(
    onBackClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.screen_create_medication_record_title))
        },
        navigationIcon = {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = AppIcons.ArrowBack,
                    contentDescription = stringResource(sharedUiR.string.shared_ui_back)
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun CreateMedicationRecordScreenPreview() {
    AppTheme {
        CreateMedicationRecordScreen(
            onBackClick = {}
        )
    }
}