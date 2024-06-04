package com.github.gasblg.stockshelper.data.repository.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.github.gasblg.stockshelper.models.SearchModel
import com.github.gasblg.stockshelper.network.AwaitResponse
import com.github.gasblg.stockshelper.network.api.ApiExchange
import com.github.gasblg.stockshelper.network.datasource.SearchSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.github.gasblg.stockshelper.network.datasource.settings.PageSettings

class SearchRepositoryImpl @Inject constructor(
    private val api: ApiExchange
) : SearchRepository {

    override suspend fun getDerivatives(ticker: String, lang: String) =
        AwaitResponse.of { api.getDerivatives(ticker, lang).toModel() }


    override suspend fun searchData(q: String, lang: String): Flow<PagingData<SearchModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PageSettings.PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchSource(api, q, lang) }
        ).flow
    }
}