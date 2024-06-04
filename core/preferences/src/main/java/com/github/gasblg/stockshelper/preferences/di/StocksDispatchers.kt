package com.github.gasblg.stockshelper.preferences.di

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val stocksDispatcher: StocksDispatchers)

enum class StocksDispatchers {
    Default,
    IO
}
