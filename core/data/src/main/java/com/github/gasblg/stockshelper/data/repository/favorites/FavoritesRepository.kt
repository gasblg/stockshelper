package com.github.gasblg.stockshelper.data.repository.favorites

import androidx.paging.PagingData
import com.github.gasblg.stockshelper.models.SharesModel
import kotlinx.coroutines.flow.Flow
import com.github.gasblg.stockshelper.common.Result


interface FavoritesRepository {

    suspend fun getDetailShares(
        ticker: String?,
        last: Int,
        lang: String
    ): Result<SharesModel>


    suspend fun getFavorites(
        sortColumn: String, sortOrder: String, lang: String
    ): Flow<PagingData<SharesModel>>

    suspend fun checkExisting(ticker: String, lang: String)


    suspend fun observeExisting(id: String?): Flow<Boolean>

}