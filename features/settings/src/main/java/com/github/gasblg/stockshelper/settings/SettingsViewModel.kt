package com.github.gasblg.stockshelper.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.gasblg.stockshelper.preferences.Preferences
import com.github.gasblg.stockshelper.preferences.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {
    val settingsUiState: StateFlow<SettingsUiState> =
        userDataRepository.userData
            .map {
                SettingsUiState.Success(settings = it)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = SettingsUiState.Loading,
            )

    fun updateLang(lang: String) {
        viewModelScope.launch {
            userDataRepository.updateLang(lang)
        }
    }

    fun updateTheme(theme: String) {
        viewModelScope.launch {
            userDataRepository.updateTheme(theme)
        }
    }
}


sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(val settings: Preferences) :
        SettingsUiState
}
