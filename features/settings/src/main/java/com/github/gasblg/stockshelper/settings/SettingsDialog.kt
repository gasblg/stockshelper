package com.github.gasblg.stockshelper.settings

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
import com.github.gasblg.stockshelper.models.enums.Theme
import com.github.gasblg.stockshelper.preferences.Preferences
import com.github.gasblg.stockshelper.models.enums.SortColumn
import com.github.gasblg.stockshelper.models.enums.SortOrder
import kotlinx.coroutines.flow.MutableStateFlow


@Composable
fun SettingsRoute(
    onDismiss: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()

    SettingsDialog(
        settingsUiState = settingsUiState,
        onDismiss = onDismiss,
        updateLang = viewModel::updateLang,
        updateTheme = viewModel::updateTheme
    )
}

@Composable
fun SettingsDialog(
    settingsUiState: SettingsUiState,
    onDismiss: () -> Unit,
    updateLang: (String) -> Unit,
    updateTheme: (String) -> Unit
) {
    val configuration = LocalConfiguration.current

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(R.string.features_settings_settings),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            HorizontalDivider()
            Column(Modifier.verticalScroll(rememberScrollState())) {
                when (settingsUiState) {
                    SettingsUiState.Loading -> {
                        Text(
                            text = stringResource(R.string.features_settings_loading),
                            modifier = Modifier.padding(vertical = 16.dp),
                        )
                    }

                    is SettingsUiState.Success -> {
                        SettingsPanel(
                            preferences = settingsUiState.settings,
                            updateLang = updateLang,
                            updateTheme = updateTheme
                        )
                    }
                }
                HorizontalDivider(Modifier.padding(top = 8.dp))
            }
        },
        confirmButton = {
            Text(
                text = stringResource(R.string.features_settings_ok),
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
private fun SettingsPanel(
    preferences: Preferences,
    updateLang: (String) -> Unit = {},
    updateTheme: (String) -> Unit = {}

) {
    SettingsDialogSectionTitle(text = stringResource(R.string.features_settings_language))
    Column(Modifier.selectableGroup()) {
        SettingsDialogThemeChooserRow(
            text = stringResource(R.string.features_settings_ru),
            selected = preferences.lang == "ru",
            onClick = {
                updateLang.invoke("ru")
            },
        )
        SettingsDialogThemeChooserRow(
            text = stringResource(R.string.features_settings_en),
            selected = preferences.lang == "en",
            onClick = {
                updateLang.invoke("en")
            },
        )
    }
    SettingsDialogSectionTitle(text = stringResource(R.string.features_settings_theme))
    Column(Modifier.selectableGroup()) {
        SettingsDialogThemeChooserRow(
            text = stringResource(R.string.features_settings_light),
            selected = preferences.theme == Theme.LIGHT.type,
            onClick = {
                updateTheme.invoke(Theme.LIGHT.type)
            },
        )
        SettingsDialogThemeChooserRow(
            text = stringResource(R.string.features_settings_dark),
            selected = preferences.theme == Theme.DARK.type,
            onClick = {
                updateTheme.invoke(Theme.DARK.type)
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
fun SettingsDialogThemeChooserRow(
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


    val uiState: MutableStateFlow<SettingsUiState> = MutableStateFlow(
        SettingsUiState.Loading
    )
    uiState.value = SettingsUiState.Success(prefs)

    val settingsState = uiState.collectAsStateWithLifecycle()


    SettingsDialog(
        settingsUiState = settingsState.value,
        onDismiss = {},
        updateLang = {},
        updateTheme = {}
    )
}
