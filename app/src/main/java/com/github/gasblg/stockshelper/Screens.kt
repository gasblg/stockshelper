package com.github.gasblg.stockshelper

sealed class Screens(val route: String) {
    data object Shares : Screens("shares_screen")
    data object Currencies : Screens("currencies_screen")
    data object News : Screens("news_screen")
}