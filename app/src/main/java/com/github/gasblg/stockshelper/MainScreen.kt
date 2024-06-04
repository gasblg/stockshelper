package com.github.gasblg.stockshelper

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.gasblg.stockshelper.currencies.CurrenciesRoute
import com.github.gasblg.stockshelper.models.SearchModel
import com.github.gasblg.stockshelper.models.SharesModel
import com.github.gasblg.stockshelper.news.DetailScreen
import com.github.gasblg.stockshelper.news.NewsRoute
import com.github.gasblg.stockshelper.news.newsNavGraph
import com.github.gasblg.stockshelper.search.navigateToSearch
import com.github.gasblg.stockshelper.search.searchScreen
import com.github.gasblg.stockshelper.bottom.BottomNavigationItem
import com.github.gasblg.stockshelper.shares.SharesRoute
import kotlinx.coroutines.flow.StateFlow

@SuppressLint("FlowOperatorInvokedInComposition", "StateFlowValueCalledInComposition")
@Composable
fun MainScreen(
    networkState: StateFlow<Boolean>,
    onStockClick: (SharesModel?) -> Unit,
    onSearchClick: (SearchModel?) -> Unit,
    ) {

    var navigationSelectedItem by rememberSaveable {
        mutableStateOf(Screens.Shares.route)
    }

    val navController = rememberNavController()
    val tabs = BottomNavigationItem().bottomNavigationItems()
    val tabsRoute = tabs.map { it.route }
    val snackBarHostState = remember { SnackbarHostState() }

    val isOffline by networkState.collectAsStateWithLifecycle()
    val noInternetConnection = stringResource(id = R.string.no_internet)

    LaunchedEffect(isOffline) {
        if (isOffline) {
            snackBarHostState.showSnackbar(
                message = noInternetConnection,
                duration = SnackbarDuration.Indefinite,
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackBarHostState) },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { _, navigationItem ->
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val navBack =
                        navBackStackEntry?.destination?.route ?: navigationSelectedItem
                    val currentRoute = if (tabsRoute.contains(navBack)) {
                        navigationSelectedItem = navBack
                        navBack
                    } else {
                        navigationSelectedItem
                    }
                    NavigationBarItem(
                        selected = currentRoute == navigationItem.route,
                        label = {
                            Text(stringResource(id = navigationItem.label))
                        },
                        icon = {
                            Icon(
                                navigationItem.icon,
                                contentDescription = stringResource(id = navigationItem.label)
                            )
                        },
                        onClick = {
                            navigationSelectedItem = navigationItem.route
                            navController.navigate(navigationItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            route = Graph.HOME,
            startDestination = Screens.Shares.route,
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
            composable(Screens.Shares.route) {
                SharesRoute(
                    !isOffline,
                    onItemClick = { model ->
                        onStockClick.invoke(model)
                    },
                    onNavigationClick = {
                       navController.navigateToSearch()
                    }
                )
            }
            composable(Screens.Currencies.route) {
                CurrenciesRoute()
            }

            composable(Screens.News.route) {
                NewsRoute(onNewsClick = { id ->
                    navController.navigate("${DetailScreen.Detail.route}/$id")
                })
            }
            newsNavGraph(navController)
            searchScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onTopicClick = { model ->
                    onSearchClick.invoke(model)
                }
            )
        }
    }


}

