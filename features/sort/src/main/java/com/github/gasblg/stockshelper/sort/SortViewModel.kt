package com.github.gasblg.stockshelper.sort

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
class SortViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    val sortUiState: StateFlow<SortUiState> =
        userDataRepository.userData
            .map {
                SortUiState.Success(settings = it)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = SortUiState.Loading,
            )

    fun orderByPercents(sortColumn: String, sortOrder: String) {
        viewModelScope.launch {
            userDataRepository.orderBy(sortColumn, sortOrder)
        }
    }
}


sealed interface SortUiState {
    data object Loading : SortUiState
    data class Success(val settings: Preferences) : SortUiState
}
