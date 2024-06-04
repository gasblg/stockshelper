package com.github.gasblg.stockshelper.data.repository.shares


import androidx.paging.PagingData
import com.github.gasblg.stockshelper.models.SharesModel
import kotlinx.coroutines.flow.Flow
import com.github.gasblg.stockshelper.common.Result



interface SharesRepository {

    suspend fun getDetailShares(
        ticker: String?,
        from: Int,
        lang: String
    ): Result<SharesModel>


    suspend fun getShares(
        sortColumn: String, sortOrder: String, lang: String
    ): Flow<PagingData<SharesModel>>


}