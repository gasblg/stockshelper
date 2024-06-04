package com.github.gasblg.stockshelper.data.repository.shares

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.github.gasblg.stockshelper.core.database.db.AppDataBase
import com.github.gasblg.stockshelper.network.AwaitResponse
import com.github.gasblg.stockshelper.network.api.ApiExchange
import com.github.gasblg.stockshelper.common.ext.asError
import com.github.gasblg.stockshelper.common.ext.asResult
import com.github.gasblg.stockshelper.network.datasource.SharesMediator
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.github.gasblg.stockshelper.common.Result
import com.github.gasblg.stockshelper.network.datasource.settings.PageSettings

class SharesRepositoryImpl @Inject constructor(
    private val database: AppDataBase,
    private val api: ApiExchange
) : SharesRepository {

    override suspend fun getDetailShares(
        ticker: String?,
        from: Int,
        lang: String
    ) = when (val result = AwaitResponse.of {
        api.getDetailShares(ticker, from, lang)
    }) {
        is Result.Error -> {
            if (database.sharesDao.checkExisting(ticker)) {
                val item = database.sharesDao.getSharesItem(ticker)
                item.toModel().asResult()
            } else {
                result.error.asError()
            }
        }

        is Result.Data -> {
            val item = result.data.toEntity()
            database.sharesDao.updateShares(item)
            item.toModel().asResult()
        }
    }


    override suspend fun getShares(sortColumn: String, sortOrder: String, lang: String) =
        pager(sortColumn, sortOrder, lang).map { pagingData ->
            pagingData.map { it.toModel() }
        }

    @OptIn(ExperimentalPagingApi::class)
    private fun pager(
        sortColumn: String, sortOrder: String, lang: String
    ) = Pager(
        config = PagingConfig(
            pageSize = PageSettings.PAGE_SIZE,
            enablePlaceholders = false
        ),
        remoteMediator = SharesMediator(
            LABEL,
            database,
            api,
            sortColumn,
            sortOrder,
            lang
        )

    ) {
        database.sharesDao.getShares()
    }.flow

    companion object {
        const val LABEL = "shares_list"
    }
}