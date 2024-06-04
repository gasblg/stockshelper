package com.github.gasblg.stockshelper.data.repository.search

import androidx.paging.PagingData
import com.github.gasblg.stockshelper.models.DerivativesModel
import com.github.gasblg.stockshelper.models.SearchModel
import kotlinx.coroutines.flow.Flow
import com.github.gasblg.stockshelper.common.Result


interface SearchRepository {

    suspend fun getDerivatives(ticker: String, lang: String): Result<DerivativesModel>

    suspend fun searchData(q: String, lang: String): Flow<PagingData<SearchModel>>

}