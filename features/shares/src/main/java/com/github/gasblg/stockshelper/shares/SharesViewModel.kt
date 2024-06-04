package com.github.gasblg.stockshelper.shares

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.github.gasblg.stockshelper.data.repository.favorites.FavoritesRepository
import com.github.gasblg.stockshelper.models.SharesModel
import com.github.gasblg.stockshelper.data.repository.shares.SharesRepository
import com.github.gasblg.stockshelper.models.enums.Language
import com.github.gasblg.stockshelper.models.enums.SortColumn
import com.github.gasblg.stockshelper.models.enums.SortOrder
import com.github.gasblg.stockshelper.preferences.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharesViewModel @Inject constructor(
    private val sharesRepository: SharesRepository,
    private val favoritesRepository: FavoritesRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {


    private val _uiState: MutableStateFlow<PagingData<SharesModel>> =
        MutableStateFlow(value = PagingData.empty())
    val uiState: StateFlow<PagingData<SharesModel>> get() = _uiState


    private val _favoritesState: MutableStateFlow<PagingData<SharesModel>> =
        MutableStateFlow(value = PagingData.empty())
    val favoritesState: StateFlow<PagingData<SharesModel>> get() = _favoritesState

    init {
        viewModelScope.launch {
            userDataRepository.userData.collect {
                load(
                    sortColumn = it.sortColumn,
                    sortOrder = it.sortOrder,
                    lang = it.lang
                )
            }
        }
    }

    fun reload() {
        viewModelScope.launch {
            val sortColumn =
                userDataRepository.userData.firstOrNull()?.sortColumn ?: SortColumn.PERCENT.type
            val sortOrder =
                userDataRepository.userData.firstOrNull()?.sortOrder ?: SortOrder.DESC.type
            val lang = userDataRepository.userData.firstOrNull()?.lang ?: Language.EN.type
            load(
                sortColumn,
                sortOrder,
                lang
            )
        }
    }


    private fun load(sortColumn: String, sortOrder: String, lang: String) {
        viewModelScope.launch {
            getShares(sortColumn, sortOrder, lang)
        }
        viewModelScope.launch {
            getFavorites(sortColumn, sortOrder, lang)
        }
    }

    private suspend fun getShares(sortColumn: String, sortOrder: String, lang: String) {
        sharesRepository.getShares(sortColumn, sortOrder, lang)
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
            .collect {
                _uiState.value = it
            }
    }

    private suspend fun getFavorites(sortColumn: String, sortOrder: String, lang: String) {
        favoritesRepository.getFavorites(sortColumn, sortOrder, lang)
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
            .collect {
                _favoritesState.value = it
            }
    }

}

