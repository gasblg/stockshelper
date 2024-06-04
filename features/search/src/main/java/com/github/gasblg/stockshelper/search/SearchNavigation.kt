package com.github.gasblg.stockshelper.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.github.gasblg.stockshelper.models.SearchModel

const val searchRoute = "search_route"

fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
    this.navigate(searchRoute, navOptions)
}

fun NavGraphBuilder.searchScreen(
    onBackClick: () -> Unit,
    onTopicClick: (SearchModel?) -> Unit,
) {

    composable(route = searchRoute) {
        SearchRoute(
            onBackClick = onBackClick,
            onTopicClick = onTopicClick,
        )
    }
}
