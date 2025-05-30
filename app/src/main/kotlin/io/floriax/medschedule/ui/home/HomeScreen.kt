package io.floriax.medschedule.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import io.floriax.medschedule.R
import io.floriax.medschedule.common.ext.collectState
import io.floriax.medschedule.ui.designsystem.AppIcons
import io.floriax.medschedule.ui.theme.AppTheme

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
@Composable
fun HomeScreen(
    onMedicationClick: () -> Unit,
    onAddMedicationRecordClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val state by viewModel.collectState()

    HomeScreen(
        state = state,
        onMedicationClick = onMedicationClick,
        onAddMedicationRecordClick = onAddMedicationRecordClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    state: HomeViewState,
    onMedicationClick: () -> Unit,
    onAddMedicationRecordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        bottomBar = {
            HomeBottomBar(
                onMedicationClick = onMedicationClick,
                onAddMedicationRecordClick = onAddMedicationRecordClick,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButtonPosition = FabPosition.EndOverlay,
    ) { paddingValues ->
        HomeContent(
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeBottomBar(
    onMedicationClick: () -> Unit,
    onAddMedicationRecordClick: () -> Unit,
    scrollBehavior: BottomAppBarScrollBehavior
) {
    BottomAppBar(
        actions = {
            IconButton(onClick = onMedicationClick) {
                Icon(
                    imageVector = AppIcons.Medication,
                    contentDescription = stringResource(R.string.home_medication)
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddMedicationRecordClick,
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(
                    imageVector = AppIcons.Add,
                    contentDescription = stringResource(R.string.home_add_medication_record)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {

    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AppTheme {
        HomeScreen(
            state = HomeViewState(),
            onMedicationClick = {},
            onAddMedicationRecordClick = {}
        )
    }
}