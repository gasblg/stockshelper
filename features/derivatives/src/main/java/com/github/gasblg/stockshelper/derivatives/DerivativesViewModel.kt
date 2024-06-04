package com.github.gasblg.stockshelper.derivatives

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.gasblg.stockshelper.data.repository.search.SearchRepository
import com.github.gasblg.stockshelper.preferences.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.github.gasblg.stockshelper.common.Result
import com.github.gasblg.stockshelper.models.DerivativesModel
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class DerivativesViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> get() = _uiState


    fun load(id: String?) {
        viewModelScope.launch {
            userDataRepository.userData.collect {
                id?.let { infoId ->
                    getDerivatives(infoId, lang = it.lang)
                }
            }
        }
    }

    private suspend fun getDerivatives(id: String, lang: String) {
        when (val response = searchRepository.getDerivatives(id, lang)) {
            is Result.Error -> {
                _uiState.value = UiState.Error(response.error)
            }

            is Result.Data -> {
                _uiState.value = UiState.Success(response.data)
            }
        }
    }


    sealed interface UiState {
        data object Loading : UiState

        data class Error(val error: Throwable) : UiState

        data class Success(val derivatives: DerivativesModel) : UiState
    }

}
