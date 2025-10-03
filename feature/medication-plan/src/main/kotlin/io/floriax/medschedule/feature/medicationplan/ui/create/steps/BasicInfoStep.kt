package io.floriax.medschedule.feature.medicationplan.ui.create.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.floriax.medschedule.feature.medicationplan.R
import io.floriax.medschedule.feature.medicationplan.ui.create.NameError
import io.floriax.medschedule.shared.designsystem.theme.AppTheme

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/10/3
 */
@Composable
fun BasicInfoStep(
    name: String,
    onNameChange: (String) -> Unit,
    nameError: NameError?,
    notes: String,
    onNotesChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = stringResource(R.string.screen_create_medication_plan_basic_info_title),
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(R.string.screen_create_medication_plan_basic_info_plan_name)) },
            supportingText = {
                val message = when (nameError) {
                    NameError.Empty -> stringResource(R.string.screen_create_medication_plan_basic_info_plan_name_error_empty)
                    null -> ""
                }
                Text(text = message)
            },
            isError = nameError != null,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true
        )

        OutlinedTextField(
            value = notes,
            onValueChange = onNotesChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(R.string.screen_create_medication_plan_basic_info_notes)) },
            minLines = 3,
            maxLines = 3
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BasicInfoStepPreview() {
    AppTheme {
        BasicInfoStep(
            name = "Medication Plan Name",
            onNameChange = {},
            nameError = null,
            notes = "Notes",
            onNotesChange = {}
        )
    }
}