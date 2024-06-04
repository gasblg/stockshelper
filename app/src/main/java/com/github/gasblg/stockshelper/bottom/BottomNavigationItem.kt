package com.github.gasblg.stockshelper.bottom

import androidx.compose.ui.graphics.vector.ImageVector
import com.github.gasblg.stockshelper.R
import com.github.gasblg.stockshelper.Screens
import com.github.gasblg.stockshelper.design.icon.StocksIcons

data class BottomNavigationItem(
    val label: Int = 0,
    val icon: ImageVector = StocksIcons.Shares,
    val route: String = ""
) {
    fun bottomNavigationItems(): List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = R.string.shares,
                icon = StocksIcons.Shares,
                route = Screens.Shares.route
            ),
            BottomNavigationItem(
                label = R.string.currencies,
                icon = StocksIcons.Ruble,
                route = Screens.Currencies.route
            ),
            BottomNavigationItem(
                label = R.string.news,
                icon = StocksIcons.News,
                route = Screens.News.route
            )
        )
    }
}