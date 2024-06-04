package com.github.gasblg.stockshelper.currencies.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.gasblg.stockshelper.data.repository.currency.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.github.gasblg.stockshelper.common.Result
import com.github.gasblg.stockshelper.models.CurrencyModel
import com.github.gasblg.stockshelper.preferences.UserDataRepository
import kotlinx.coroutines.flow.StateFlow


@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val currencyRepository: CurrencyRepository
) : ViewModel() {


    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> get() = _uiState


    fun load(id: String?) {
        viewModelScope.launch {
            userDataRepository.userData.collect {
                id?.let { infoId ->
                    getCurrency(infoId, lang = it.lang)
                }
            }
        }
    }


    private suspend fun getCurrency(id: String, lang: String) {
        when (val response = currencyRepository.getCurrency(id, lang)) {
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

        data class Success(val currencyModel: CurrencyModel) :
            UiState
    }

}
