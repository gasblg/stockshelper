package com.github.gasblg.stockshelper.sort

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.gasblg.stockshelper.preferences.Preferences
import com.github.gasblg.stockshelper.models.enums.SortColumn
import com.github.gasblg.stockshelper.models.enums.SortOrder
import kotlinx.coroutines.flow.MutableStateFlow


@Composable
fun SortRoute(
    onDismiss: () -> Unit,
    viewModel: SortViewModel = hiltViewModel()
) {
    val sortUiState by viewModel.sortUiState.collectAsStateWithLifecycle()

    SortDialog(
        sortUiState = sortUiState,
        onDismiss = onDismiss,
        orderByPercents = viewModel::orderByPercents
    )
}


@Composable
fun SortDialog(
    sortUiState: SortUiState,
    onDismiss: () -> Unit,
    orderByPercents: (String, String) -> Unit
) {
    val configuration = LocalConfiguration.current

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(R.string.features_sort_order_by),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            HorizontalDivider()
            Column(Modifier.verticalScroll(rememberScrollState())) {
                when (sortUiState) {
                    SortUiState.Loading -> {
                        Text(
                            text = stringResource(R.string.features_sort_loading),
                            modifier = Modifier.padding(vertical = 16.dp),
                        )
                    }

                    is SortUiState.Success -> {
                        SortPanel(
                            preferences = sortUiState.settings,
                            orderByPercents = orderByPercents
                        )
                    }
                }
                HorizontalDivider(Modifier.padding(top = 8.dp))
            }
        },
        confirmButton = {
            Text(
                text = stringResource(R.string.features_sort_ok),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismiss() },
            )
        },
    )
}


@Composable
private fun SortPanel(
    preferences: Preferences,
    orderByPercents: (String, String) -> Unit
) {
    SettingsDialogSectionTitle(text = stringResource(R.string.features_sort_name))
    Column(Modifier.selectableGroup()) {
        SortDialogThemeChooserRow(
            text = stringResource(R.string.features_sort_asc_alphabet),
            selected = (preferences.sortColumn == SortColumn.NAME.type) && (preferences.sortOrder == SortOrder.ASC.type),
            onClick = {
                orderByPercents.invoke(SortColumn.NAME.type, SortOrder.ASC.type)
            },
        )
        SortDialogThemeChooserRow(
            text = stringResource(R.string.features_sort_desc_alphabet),
            selected = (preferences.sortColumn == SortColumn.NAME.type) && (preferences.sortOrder == SortOrder.DESC.type),
            onClick = {
                orderByPercents.invoke(SortColumn.NAME.type, SortOrder.DESC.type)
            },
        )
    }

    SettingsDialogSectionTitle(text = stringResource(R.string.features_sort_percents))
    Column(Modifier.selectableGroup()) {
        SortDialogThemeChooserRow(
            text = stringResource(R.string.features_sort_asc),
            selected = (preferences.sortColumn == SortColumn.PERCENT.type) && (preferences.sortOrder == SortOrder.ASC.type),
            onClick = {
                orderByPercents.invoke(SortColumn.PERCENT.type, SortOrder.ASC.type)
            },
        )
        SortDialogThemeChooserRow(
            text = stringResource(R.string.features_sort_desc),
            selected = (preferences.sortColumn == SortColumn.PERCENT.type) && (preferences.sortOrder == SortOrder.DESC.type),
            onClick = {
                orderByPercents.invoke(SortColumn.PERCENT.type, SortOrder.DESC.type)
            },
        )
    }

    SettingsDialogSectionTitle(text = stringResource(R.string.features_sort_ticker))
    Column(Modifier.selectableGroup()) {
        SortDialogThemeChooserRow(
            text = stringResource(R.string.features_sort_asc_alphabet),
            selected = (preferences.sortColumn == SortColumn.TICKER.type) && (preferences.sortOrder == SortOrder.ASC.type),
            onClick = {
                orderByPercents.invoke(SortColumn.TICKER.type, SortOrder.ASC.type)
            },
        )
        SortDialogThemeChooserRow(
            text = stringResource(R.string.features_sort_desc_alphabet),
            selected = (preferences.sortColumn == SortColumn.TICKER.type) && (preferences.sortOrder == SortOrder.DESC.type),
            onClick = {
                orderByPercents.invoke(SortColumn.TICKER.type, SortOrder.DESC.type)
            },
        )
    }
}


@Composable
private fun SettingsDialogSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
    )
}


@Composable
fun SortDialogThemeChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Preview
@Composable
fun SortDialogPreview() {
    val prefs = Preferences(
        sortColumn = SortColumn.NAME.type,
        sortOrder = SortOrder.ASC.type,
        lang = "en",
        theme = "dark",
    )

    val uiState: MutableStateFlow<SortUiState> = MutableStateFlow(
        SortUiState.Loading
    )
    uiState.value = SortUiState.Success(prefs)

    val sortState = uiState.collectAsStateWithLifecycle()

    SortDialog(
        sortUiState = sortState.value,
        onDismiss = {},
        orderByPercents = { _: String, _: String -> }
    )
}



