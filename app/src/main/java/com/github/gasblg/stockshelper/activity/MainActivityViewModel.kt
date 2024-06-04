package com.github.gasblg.stockshelper.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.gasblg.stockshelper.preferences.Preferences
import com.github.gasblg.stockshelper.preferences.UserDataRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    userDataRepositoryImpl: UserDataRepositoryImpl,
) : ViewModel() {

    val uiState: StateFlow<MainActivityUiState> = userDataRepositoryImpl.userData.map {
        MainActivityUiState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = MainActivityUiState.Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )
}
    sealed interface MainActivityUiState {
        data object Loading : MainActivityUiState
        data class Success(val prefs: Preferences) : MainActivityUiState
    }


