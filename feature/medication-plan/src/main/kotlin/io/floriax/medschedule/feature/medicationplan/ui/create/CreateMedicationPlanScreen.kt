package io.floriax.medschedule.feature.medicationplan.ui.create

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import io.floriax.medschedule.feature.medicationplan.R
import io.floriax.medschedule.shared.designsystem.icon.AppIcons
import io.floriax.medschedule.shared.designsystem.theme.AppTheme
import io.floriax.medschedule.shared.ui.component.BackButton
import io.floriax.medschedule.shared.ui.extension.collectState
import io.floriax.medschedule.shared.ui.R as sharedUiR

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/10/3
 */
@Composable
fun CreateMedicationPlanRoute(
    onBackClick: () -> Unit,
    viewModel: CreateMedicationPlanViewModel = hiltViewModel()
) {
    val uiState by viewModel.collectState()

    CreateMedicationPlanScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onPreviousClick = viewModel::onPreviousStepClick,
        onNextClick = viewModel::onNextStepClick
    )
}

@Composable
private fun CreateMedicationPlanScreen(
    uiState: CreateMedicationPlanUiState,
    onBackClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CreateMedicationPlanTopBar(onBackClick = onBackClick)
        },
        bottomBar = {
            CreateMedicationPlanBottomBar(
                uiState = uiState,
                onPreviousClick = onPreviousClick,
                onNextClick = onNextClick
            )
        }
    ) { paddingValues ->
        CreateMedicationPlanContent(
            uiState = uiState,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateMedicationPlanTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.screen_create_medication_plan_title))
        },
        modifier = modifier,
        navigationIcon = {
            BackButton(onClick = onBackClick)
        }
    )
}

@Composable
private fun CreateMedicationPlanContent(
    uiState: CreateMedicationPlanUiState,
    modifier: Modifier = Modifier
) {
    val progress =
        (uiState.currentStep.ordinal + 1) / CreateMedicationPlanStep.entries.size.toFloat()
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
    )

    Column(modifier = modifier.fillMaxSize()) {
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxWidth()
        )
        AnimatedContent(
            targetState = uiState.currentStep,
            label = "CreateMedicationPlanStepAnimation",
            transitionSpec = {
                if (targetState.ordinal > initialState.ordinal) {
                    slideInHorizontally { fullWidth -> fullWidth } + fadeIn() togetherWith
                            slideOutHorizontally { fullWidth -> -fullWidth } + fadeOut()
                } else {
                    slideInHorizontally { fullWidth -> -fullWidth } + fadeIn() togetherWith
                            slideOutHorizontally { fullWidth -> fullWidth } + fadeOut()
                }
            }
        ) { targetStep ->
            when (targetStep) {
                CreateMedicationPlanStep.BASIC_INFO -> BasicInfoStep(modifier = Modifier.fillMaxSize())
                CreateMedicationPlanStep.SCHEDULE_TYPE -> ScheduleTypeStep(modifier = Modifier.fillMaxSize())
                CreateMedicationPlanStep.DOSAGE -> DosageStep(modifier = Modifier.fillMaxSize())
                CreateMedicationPlanStep.SAVE -> SaveStep(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun CreateMedicationPlanBottomBar(
    uiState: CreateMedicationPlanUiState,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        actions = {
            if (uiState.showPreviousStep) {
                TextButton(onClick = onPreviousClick) {
                    Text(text = stringResource(sharedUiR.string.shared_ui_previous))
                }
            }
        },
        modifier = modifier,
        floatingActionButton = {
            val (textRes, iconRes) = if (uiState.showSaveButton) {
                sharedUiR.string.shared_ui_save to AppIcons.Check
            } else {
                sharedUiR.string.shared_ui_next to AppIcons.ArrowForward
            }
            val text = stringResource(textRes)
            ExtendedFloatingActionButton(
                text = { Text(text = text) },
                icon = { Icon(imageVector = iconRes, contentDescription = text) },
                onClick = onNextClick
            )
        }
    )
}

@Composable
private fun BasicInfoStep(modifier: Modifier = Modifier) {

}

@Composable
private fun ScheduleTypeStep(modifier: Modifier = Modifier) {

}

@Composable
private fun DosageStep(modifier: Modifier = Modifier) {

}

@Composable
private fun SaveStep(modifier: Modifier = Modifier) {

}

@Preview(showBackground = true)
@Composable
private fun CreateMedicationPlanPreview() {
    AppTheme {
        CreateMedicationPlanScreen(
            uiState = CreateMedicationPlanUiState(),
            onBackClick = {},
            onPreviousClick = {},
            onNextClick = {}
        )
    }
}
