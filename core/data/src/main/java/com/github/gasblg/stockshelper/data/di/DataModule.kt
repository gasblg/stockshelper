package com.github.gasblg.stockshelper.data.di

import com.github.gasblg.stockshelper.data.repository.currency.CurrencyRepository
import com.github.gasblg.stockshelper.data.repository.currency.CurrencyRepositoryImpl
import com.github.gasblg.stockshelper.data.repository.favorites.FavoritesRepository
import com.github.gasblg.stockshelper.data.repository.favorites.FavoritesRepositoryImpl
import com.github.gasblg.stockshelper.data.repository.news.NewsRepository
import com.github.gasblg.stockshelper.data.repository.news.NewsRepositoryImpl
import com.github.gasblg.stockshelper.data.repository.search.SearchRepository
import com.github.gasblg.stockshelper.data.repository.search.SearchRepositoryImpl
import com.github.gasblg.stockshelper.data.repository.shares.SharesRepository
import com.github.gasblg.stockshelper.data.repository.shares.SharesRepositoryImpl

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsCurrencyRepository(
        currencyRepository: CurrencyRepositoryImpl
    ): CurrencyRepository

    @Binds
    fun bindsSharesRepository(
        sharesRepository: SharesRepositoryImpl
    ): SharesRepository

    @Binds
    fun bindsFavoritesRepository(
        favoritesRepository: FavoritesRepositoryImpl
    ): FavoritesRepository

    @Binds
    fun bindsNewsRepository(
        newsRepository: NewsRepositoryImpl
    ): NewsRepository

    @Binds
    fun bindsSearchRepository(
        searchRepository: SearchRepositoryImpl
    ): SearchRepository



}
