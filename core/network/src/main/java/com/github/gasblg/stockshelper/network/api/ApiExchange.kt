package com.github.gasblg.stockshelper.network.api

import com.github.gasblg.stockshelper.network.datasource.settings.PageSettings
import com.github.gasblg.stockshelper.network.responses.CurrencyResponse
import com.github.gasblg.stockshelper.network.responses.DerivativesResponse
import com.github.gasblg.stockshelper.network.responses.NewsResponse
import com.github.gasblg.stockshelper.network.responses.PaginationResponse
import com.github.gasblg.stockshelper.network.responses.SearchResponse
import com.github.gasblg.stockshelper.network.responses.SharesResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiExchange {

    @GET("/shares")
    suspend fun getShares(
        @Query("sort_column") sortColumn: String,
        @Query("sort_order") sortOrder: String?,
        @Query("lang") lang: String,
        @Query("page_size") pageSize: Int? = PageSettings.PAGE_SIZE,
        @Query("page") page: Int? = null
    ): PaginationResponse<SharesResponse>

    @GET("/shares/{ticker}")
    suspend fun getDetailShares(
        @Path("ticker") ticker: String?,
        @Query("last") last: Int,
        @Query("lang") lang: String
    ): SharesResponse

    @POST("/favorites/{ticker}")
    suspend fun setFavorite(
        @Path("ticker") ticker: String,
        @Query("lang") lang: String
    ): SharesResponse

    @DELETE("/favorites/{ticker}")
    suspend fun removeFavorite(
        @Path("ticker") ticker: String
    ): String

    @GET("/favorites")
    suspend fun getFavorites(
        @Query("sort_column") sortColumn: String,
        @Query("sort_order") sortOrder: String?,
        @Query("lang") lang: String,
        @Query("page_size") pageSize: Int? = PageSettings.PAGE_SIZE,
        @Query("page") page: Int? = null
    ): PaginationResponse<SharesResponse>

    @GET("/currencies")
    suspend fun getCurrencies(
        @Query("lang") lang: String
    ): List<CurrencyResponse>

    @GET("/currencies/{ticker}")
    suspend fun getCurrency(
        @Path("ticker") ticker: String,
        @Query("lang") lang: String
    ): CurrencyResponse

    @GET("/news")
    suspend fun getNewsList(
        @Query("lang") lang: String,
        @Query("page_size") pageSize: Int? = PageSettings.PAGE_SIZE,
        @Query("page") page: Int? = null
    ): PaginationResponse<NewsResponse>

    @GET("/news/{id}")
    suspend fun getDetailNews(
        @Path(value = "id") newsId: Int,
        @Query("lang") lang: String
    ): NewsResponse

    @GET("/search")
    suspend fun searchData(
        @Query("q") q: String,
        @Query("lang") lang: String,
        @Query("page_size") pageSize: Int? = PageSettings.PAGE_SIZE,
        @Query("page") page: Int? = null
    ): PaginationResponse<SearchResponse>

    @GET("/derivatives/{ticker}")
    suspend fun getDerivatives(
        @Path("ticker") ticker: String,
        @Query("lang") lang: String
    ): DerivativesResponse
}