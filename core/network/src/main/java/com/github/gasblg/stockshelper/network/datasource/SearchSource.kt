package com.github.gasblg.stockshelper.network.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.gasblg.stockshelper.network.api.ApiExchange
import com.github.gasblg.stockshelper.models.SearchModel
import com.github.gasblg.stockshelper.network.datasource.settings.PageSettings
import javax.inject.Inject


class SearchSource @Inject constructor(
    private val api: ApiExchange,
    private val q: String,
    private val lang: String
) : PagingSource<Int, SearchModel>() {


    override fun getRefreshKey(state: PagingState<Int, SearchModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchModel> {
        val page = params.key ?: PageSettings.DEFAULT_START
        return try {
            val response = api.searchData(q = q, lang = lang, page = page)
            val searchList = response.results.map { it.toModel() }
            val next = response.next
            val nextKey = if (next == 0 || next == null) {
                null
            } else {
                page.plus(1)
            }
            LoadResult.Page(
                data = searchList,
                prevKey = if (page == PageSettings.DEFAULT_START) null else page,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

}