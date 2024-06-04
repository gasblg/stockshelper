package com.github.gasblg.stockshelper.core.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.github.gasblg.stockshelper.core.database.entities.SharesEntity

@Dao
interface SharesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShares(list: List<SharesEntity>)

    @Query("SELECT * FROM shares")
    fun getShares(): PagingSource<Int, SharesEntity>

    @Query("SELECT * FROM shares WHERE ticker = :ticker")
    suspend fun getSharesItem(ticker: String?): SharesEntity

    @Update
    suspend fun updateShares(sharesEntity: SharesEntity)

    @Query("DELETE from shares")
    fun deleteAll()

    @Query("SELECT EXISTS (SELECT 1 FROM shares WHERE ticker = :ticker)")
    suspend fun checkExisting(ticker: String?): Boolean
}