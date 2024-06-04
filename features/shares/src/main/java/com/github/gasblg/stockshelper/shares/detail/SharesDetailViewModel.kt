package com.github.gasblg.stockshelper.shares.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.gasblg.stockshelper.data.repository.shares.SharesRepository
import com.github.gasblg.stockshelper.data.repository.favorites.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.github.gasblg.stockshelper.common.Result
import com.github.gasblg.stockshelper.data.utils.date.DateConverter
import com.github.gasblg.stockshelper.models.SharesModel
import com.github.gasblg.stockshelper.preferences.UserDataRepository
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class SharesDetailViewModel @Inject constructor(
    private val sharesRepository: SharesRepository,
    private val favoritesRepository: FavoritesRepository,
    private val userDataRepository: UserDataRepository,
    private val dateConverter: DateConverter
) : ViewModel() {


    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> get() = _uiState


    private val _existingState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val existingState: StateFlow<Boolean> get() = _existingState

    fun checkExisting(ticker: String) {
        viewModelScope.launch {
            userDataRepository.userData.collect {
                favoritesRepository.checkExisting(ticker, it.lang)
            }

        }
    }

    suspend fun observeExisting(ticker: String) {
        viewModelScope.launch {
            favoritesRepository.observeExisting(ticker).collect {
                _existingState.value = it
            }
        }
    }

    fun getDay(date: String) = dateConverter.getDay(date)

    fun getDayOrYesterday(date: String) = dateConverter.getDayOrYesterday(date)


    suspend fun start(ticker: String) {
        userDataRepository.userData.collect {
            viewModelScope.launch { getStockInfo(ticker, it.lang) }
        }
    }

    private suspend fun getStockInfo(ticker: String, lang: String) {
        when (val response =
            sharesRepository.getDetailShares(ticker, 60, lang)) {
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

        data class Success(val sharesModel: SharesModel) : UiState
    }
}
