package com.github.gasblg.stockshelper.core.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.github.gasblg.stockshelper.core.database.entities.FavoritesEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoritesDao {

    @Query("SELECT * FROM favorites")
    fun getFavorites(): PagingSource<Int, FavoritesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorite(favoritesEntity: FavoritesEntity)

    @Query("DELETE FROM favorites WHERE ticker = :ticker")
    suspend fun deleteFavorite(ticker: String)

    @Query("SELECT EXISTS (SELECT 1 FROM favorites WHERE ticker = :ticker)")
    suspend fun checkExisting(ticker: String?): Boolean

    @Query("SELECT EXISTS (SELECT 1 FROM favorites WHERE ticker = :ticker)")
    fun observeExisting(ticker: String?): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShares(list: List<FavoritesEntity>)

    @Query("DELETE from favorites")
    fun deleteAll()
}