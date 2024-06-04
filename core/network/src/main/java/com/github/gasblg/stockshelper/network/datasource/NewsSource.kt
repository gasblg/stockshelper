package com.github.gasblg.stockshelper.network.datasource


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.gasblg.stockshelper.models.NewsModel
import com.github.gasblg.stockshelper.network.api.ApiExchange
import com.github.gasblg.stockshelper.network.datasource.settings.PageSettings

class NewsSource(
    private val lang: String,
    private val api: ApiExchange
) : PagingSource<Int, NewsModel>() {
    override fun getRefreshKey(state: PagingState<Int, NewsModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsModel> {
        val page = params.key ?: PageSettings.DEFAULT_START
        return try {
            val response = api.getNewsList(lang = lang, page = page)
            val newsList = response.results.map { it.toModel() }
            val next = response.next

            val nextKey = if (next == 0 || next == null) {
                null
            } else {
                page.plus(1)
            }

            LoadResult.Page(
                data = newsList,
                prevKey = if (page == PageSettings.DEFAULT_START) null else page,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}