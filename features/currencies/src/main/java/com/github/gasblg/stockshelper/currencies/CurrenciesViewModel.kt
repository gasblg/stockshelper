package com.github.gasblg.stockshelper.currencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.github.gasblg.stockshelper.preferences.UserDataRepository
import com.github.gasblg.stockshelper.data.repository.currency.CurrencyRepository
import kotlinx.coroutines.flow.firstOrNull
import com.github.gasblg.stockshelper.common.Result
import com.github.gasblg.stockshelper.models.CurrencyModel
import com.github.gasblg.stockshelper.models.enums.Language
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class CurrenciesViewModel @Inject constructor(
    private val dataRepository: CurrencyRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {


    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> get() = _uiState


    init {
        viewModelScope.launch {
            userDataRepository.userData.collect {
                viewModelScope.launch {
                    getCurrency(lang = it.lang)
                }
            }
        }
    }

    fun reload() {
        viewModelScope.launch {
            val lang = userDataRepository.userData.firstOrNull()?.lang ?: Language.EN.type
            getCurrency(lang)
        }
    }

    private suspend fun getCurrency(lang: String) {
        when (val response = dataRepository.getCurrencies(lang)) {
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
        data class Success(val currency: List<CurrencyModel>) : UiState
    }
}
