package com.github.gasblg.stockshelper.data.repository.news

import androidx.paging.PagingData
import com.github.gasblg.stockshelper.models.NewsModel
import kotlinx.coroutines.flow.Flow
import com.github.gasblg.stockshelper.common.Result


interface NewsRepository {

    suspend fun getNewsList(lang: String): Flow<PagingData<NewsModel>>

    suspend fun getNews(id: Int, lang: String): Result<NewsModel>

}