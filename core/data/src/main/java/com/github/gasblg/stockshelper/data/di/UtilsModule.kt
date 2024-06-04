package com.github.gasblg.stockshelper.data.di

import com.github.gasblg.stockshelper.data.utils.date.DateConverter
import com.github.gasblg.stockshelper.data.utils.date.DateConverterImpl
import com.github.gasblg.stockshelper.data.utils.network.NetworkMonitor
import com.github.gasblg.stockshelper.data.utils.network.NetworkMonitorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface UtilsModule {

    @Binds
    fun bindsNetworkMonitor(
        networkMonitor: NetworkMonitorImpl
    ): NetworkMonitor

    @Binds
    fun bindsDateConvertor(
        dateConverter: DateConverterImpl
    ): DateConverter

}
