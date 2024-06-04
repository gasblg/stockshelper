package com.github.gasblg.stockshelper.news.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.gasblg.stockshelper.data.repository.news.NewsRepository
import com.github.gasblg.stockshelper.preferences.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.github.gasblg.stockshelper.common.Result
import com.github.gasblg.stockshelper.models.NewsModel
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {


    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> get() = _uiState


    fun load(id: Int) {
        viewModelScope.launch {
            userDataRepository.userData.collect {
                getNews(id, lang = it.lang)
            }
        }
    }

    private suspend fun getNews(id: Int, lang: String) {
        when (val response = newsRepository.getNews(id, lang)) {
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
        data class Success(val news: NewsModel) : UiState
    }

}
