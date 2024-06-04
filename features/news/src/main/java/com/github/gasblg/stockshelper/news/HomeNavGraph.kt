package com.github.gasblg.stockshelper.news

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.gasblg.stockshelper.news.detail.DetailRoute

const val newsRoute = "news_route"


fun NavGraphBuilder.newsNavGraph(navController: NavHostController) {
    navigation(
        route = newsRoute,
        startDestination = DetailScreen.Detail.route
    ) {
        composable("${DetailScreen.Detail.route}/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            id?.let {
                DetailRoute(
                    it,
                    onBackClick = { navController.popBackStack() })
            }
        }
    }
}

sealed class DetailScreen(val route: String) {
    data object Detail : DetailScreen(route = "detail_news")
}