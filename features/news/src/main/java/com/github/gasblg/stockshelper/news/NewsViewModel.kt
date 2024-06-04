package com.github.gasblg.stockshelper.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.github.gasblg.stockshelper.data.repository.news.NewsRepository
import com.github.gasblg.stockshelper.models.NewsModel
import com.github.gasblg.stockshelper.models.enums.Language
import com.github.gasblg.stockshelper.preferences.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {


    private val _uiState: MutableStateFlow<PagingData<NewsModel>> =
        MutableStateFlow(value = PagingData.empty())
    val uiState: StateFlow<PagingData<NewsModel>> get() = _uiState

    init {
        viewModelScope.launch {
            userDataRepository.userData.collect {
                viewModelScope.launch {
                    getNews(it.lang)
                }
            }
        }
    }

    fun reload() {
        viewModelScope.launch {
            val lang = userDataRepository.userData.firstOrNull()?.lang ?: Language.EN.type
            getNews(lang)
        }
    }

    private suspend fun getNews(lang: String) {
        newsRepository.getNewsList(lang)
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
            .collectLatest {
                _uiState.value = it
            }
    }
}
