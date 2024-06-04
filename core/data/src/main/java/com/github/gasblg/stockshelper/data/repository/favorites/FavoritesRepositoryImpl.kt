package com.github.gasblg.stockshelper.data.repository.favorites

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.github.gasblg.stockshelper.core.database.db.AppDataBase
import com.github.gasblg.stockshelper.network.AwaitResponse
import com.github.gasblg.stockshelper.network.api.ApiExchange
import com.github.gasblg.stockshelper.network.datasource.FavoritesMediator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.github.gasblg.stockshelper.common.ext.asError
import com.github.gasblg.stockshelper.common.ext.asResult
import com.github.gasblg.stockshelper.common.Result
import com.github.gasblg.stockshelper.network.datasource.settings.PageSettings

class FavoritesRepositoryImpl @Inject constructor(
    private val database: AppDataBase,
    private val api: ApiExchange
) : FavoritesRepository {

    override suspend fun getDetailShares(
        ticker: String?,
        last: Int,
        lang: String
    ) = when (val result = AwaitResponse.of {
        api.getDetailShares(ticker, last, lang)
    }) {
        is Result.Error -> result.error.asError()
        is Result.Data -> {
            val item = result.data.toEntity()
            item.toModel().asResult()
        }
    }


    override suspend fun getFavorites(sortColumn: String, sortOrder: String, lang: String) =
        pager(sortColumn, sortOrder, lang).map { pagingData ->
            pagingData.map {
                it.toModel()
            }
        }

    override suspend fun checkExisting(ticker: String, lang: String) {
        val isExist = database.favoritesDao.checkExisting(ticker)
        if (isExist) {
            removeFavorite(ticker)
        } else {
            addToFavorite(ticker, lang)
        }
    }

    private suspend fun removeFavorite(ticker: String) {
        when (val result = AwaitResponse.of {
            api.removeFavorite(ticker)
        }) {
            is Result.Error -> result.error
            is Result.Data -> database.favoritesDao.deleteFavorite(result.data)
        }
    }

    private suspend fun addToFavorite(ticker: String, lang: String) {
        when (val result = AwaitResponse.of {
            api.setFavorite(ticker, lang)
        }) {
            is Result.Error -> result.error
            is Result.Data -> database.favoritesDao.addToFavorite(
                result.data.toFavoritesEntity()
            )
        }
    }

    override suspend fun observeExisting(id: String?): Flow<Boolean> =
        database.favoritesDao.observeExisting(id)


    @OptIn(ExperimentalPagingApi::class)
    private fun pager(
        sortColumn: String, sortOrder: String, lang: String
    ) = Pager(
        config = PagingConfig(
            pageSize = PageSettings.PAGE_SIZE,
            enablePlaceholders = false
        ),
        remoteMediator = FavoritesMediator(
            LABEL,
            database,
            api,
            sortColumn,
            sortOrder,
            lang
        )

    ) {
        database.favoritesDao.getFavorites()
    }.flow

    companion object {
        const val LABEL = "favorites_list"
    }
}