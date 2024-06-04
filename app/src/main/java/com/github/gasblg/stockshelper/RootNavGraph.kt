package com.github.gasblg.stockshelper

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.gasblg.stockshelper.currencies.detail.CurrencyRoute
import com.github.gasblg.stockshelper.models.enums.Market
import com.github.gasblg.stockshelper.data.utils.network.NetworkMonitor
import com.github.gasblg.stockshelper.derivatives.SearchInfoScreen
import com.github.gasblg.stockshelper.shares.detail.SharesDetailRoute
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun RootNavGraph(
    networkMonitor: NetworkMonitor,
    windowSizeClass: WindowSizeClass,
    appState: StocksAppState = rememberStocksAppState(
        networkMonitor = networkMonitor,
        windowSizeClass = windowSizeClass
    ),
) {
    NavHost(
        navController = appState.navController,
        startDestination = RootScreen.Main.route,
        route = Graph.ROOT
    ) {
        composable(RootScreen.Main.route) {
            MainScreen(appState.isOffline, { model ->
                    appState.navController.navigate("${SharesScreen.SharesDetail.route}/${model?.ticker}")
                }
            ) { searchModel ->
                val encodeTicker =
                    URLEncoder.encode(searchModel?.ticker, StandardCharsets.UTF_8.toString())

                when (searchModel?.market) {
                    Market.STOCK.value -> appState.navController.navigate("${SharesScreen.SharesDetail.route}/${encodeTicker}")
                    Market.CURRENCY.value -> appState.navController.navigate("${SharesScreen.Currency.route}/${encodeTicker}")
                    else -> appState.navController.navigate("${SharesScreen.Search.route}/${encodeTicker}")
                }
            }
        }

        composable("${SharesScreen.SharesDetail.route}/{ticker}") { backStackEntry ->
            SharesDetailRoute(
                backStackEntry.arguments?.getString("ticker"),
                onBackClick = { appState.navController.popBackStack() })
        }

        composable("${SharesScreen.Currency.route}/{ticker}") { backStackEntry ->
            CurrencyRoute(
                backStackEntry.arguments?.getString("ticker"),
                onBackClick = { appState.navController.popBackStack() })
        }

        composable("${SharesScreen.Search.route}/{ticker}") { backStackEntry ->
            SearchInfoScreen(
                backStackEntry.arguments?.getString("ticker"),
                onBackClick = { appState.navController.popBackStack() })
        }

    }
}

sealed class RootScreen(val route: String) {
    data object Main : RootScreen(route = "main")

}

sealed class SharesScreen(val route: String) {
    data object SharesDetail : SharesScreen(route = "shares_detail")
    data object Currency : SharesScreen(route = "currency")
    data object Search : SharesScreen(route = "search")

}