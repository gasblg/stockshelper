package com.github.gasblg.stockshelper.data.repository.news

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.github.gasblg.stockshelper.models.NewsModel
import com.github.gasblg.stockshelper.network.AwaitResponse
import com.github.gasblg.stockshelper.network.api.ApiExchange
import com.github.gasblg.stockshelper.network.datasource.NewsSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.github.gasblg.stockshelper.common.Result
import com.github.gasblg.stockshelper.network.datasource.settings.PageSettings

class NewsRepositoryImpl @Inject constructor(
    private val api: ApiExchange
) : NewsRepository {


    override suspend fun getNewsList(lang: String): Flow<PagingData<NewsModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PageSettings.PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NewsSource(lang, api) }
        ).flow
    }


    override suspend fun getNews(id: Int, lang: String): Result<NewsModel> =
        AwaitResponse.of { api.getDetailNews(id, lang).toModel() }


}