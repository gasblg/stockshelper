package com.github.gasblg.stockshelper.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.github.gasblg.stockshelper.data.repository.search.SearchRepository
import com.github.gasblg.stockshelper.models.SearchModel
import com.github.gasblg.stockshelper.preferences.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository,
    private val savedStateHandle: SavedStateHandle,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = "")


    private val _uiState: MutableStateFlow<PagingData<SearchModel>> =
        MutableStateFlow(value = PagingData.empty())
    val uiState: StateFlow<PagingData<SearchModel>> get() = _uiState

    fun onSearchQueryChanged(query: String) {
        savedStateHandle[SEARCH_QUERY] = query
    }

    fun onSearchTriggered(query: String) {
        viewModelScope.launch {
            userDataRepository.userData.collect {
                repository.searchData(query, lang = it.lang)
                    .distinctUntilChanged()
                    .cachedIn(viewModelScope)
                    .collect { data ->
                        _uiState.value = data
                    }
            }
        }
    }


    fun clearRecentSearches() {
        viewModelScope.launch {
            _uiState.value = PagingData.empty()
        }
    }
}

private const val SEARCH_QUERY = "searchQuery"
