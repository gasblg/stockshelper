package com.github.gasblg.stockshelper.news

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.gasblg.stockshelper.design.component.BottomProgressView
import com.github.gasblg.stockshelper.design.component.EmptyReloadView
import com.github.gasblg.stockshelper.design.component.ProgressView
import com.github.gasblg.stockshelper.design.icon.StocksIcons
import com.github.gasblg.stockshelper.models.NewsModel
import com.github.gasblg.stockshelper.settings.SettingsRoute
import kotlinx.coroutines.flow.MutableStateFlow


@Composable
fun NewsRoute(
    onNewsClick: (Int) -> Unit,
    viewModel: NewsViewModel = hiltViewModel()
) {
    val state: LazyPagingItems<NewsModel> = viewModel.uiState.collectAsLazyPagingItems()

    NewsScreen(
        state,
        onNewsClick,
        viewModel::reload
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    state: LazyPagingItems<NewsModel>,
    onNewsClick: (Int) -> Unit,
    reloadClick: () -> Unit
) {

    var showSettingsDialog by rememberSaveable {
        mutableStateOf(false)
    }
    if (showSettingsDialog) {
        SettingsRoute(
            onDismiss = { showSettingsDialog = false }
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopAppBar(
            colors = topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            title = {
                Text(text = stringResource(id = R.string.features_news_news))
            },
            actions = {
                IconButton(onClick = {
                    showSettingsDialog = true
                }) {
                    Icon(StocksIcons.Settings, contentDescription = null)
                }

            },
        )
        state.apply {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 4.dp, bottom = 8.dp)
            ) {

                items(itemCount) { index ->
                    NewsItem(
                        model = state[index],
                        onNewsClick
                    )
                }

                when (loadState.refresh) {
                    is LoadState.Loading -> {
                        item {
                            ProgressView(Modifier.fillParentMaxSize())
                        }
                    }

                    is LoadState.Error -> {
                        item {
                            val errorMessage =
                                (loadState.refresh as LoadState.Error).error.message ?: ""
                            EmptyReloadView(
                                errorMessage = errorMessage,
                                modifier = Modifier.fillParentMaxSize(),
                                reloadClick = { reloadClick.invoke() })
                        }
                    }

                    else -> {}
                }
                when (loadState.append) {
                    is LoadState.Error -> {}

                    is LoadState.Loading -> {
                        item {
                            BottomProgressView()
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Preview
@Composable
fun NewsScreenPreview() {
    val newsModel = NewsModel(
        id = 14,
        date = "2024-03-22",
        title = "Japanese Yen Hovers Near 4-Month Lows",
        body = "The Japanese yen held around 151.5 per dollar, hovering near its lowest levels in four months as the dollar strengthened on bets that US Federal Reserve monetary policy could remain restrictive even as other major central banks start cutting interest rates." + "<br>" +
                "Domestically, investors digested data showing Japan’s headline and core inflation rates both rose to a four-month high of 2.8% in February, supporting the Bank of Japan’s decision to end negative rates." + "<br>" +
                "Earlier this week, the BOJ raised rates from -0.1% to 0%, hiking for the first time since 2007 and ending eight years of negative rates amid rising wages and high inflation." + "<br>" +
                "The central bank also abandoned its yield curve control policy, ended ETF and J-REIT purchases and reduced bond buying activities." + "<br>" +
                "However, BOJ Governor Kazuo Ueda said the central bank will maintain an accommodative stance for some time, keeping rates at 0%.",
    )

    val uiState: MutableStateFlow<PagingData<NewsModel>> =
        MutableStateFlow(value = PagingData.empty())
    uiState.value = PagingData.from(listOf(newsModel))
    val newsState = uiState.collectAsLazyPagingItems()

    NewsScreen(
        state = newsState,
        onNewsClick = {},
        reloadClick = {}
    )

}

@Preview
@Composable
private fun EmptyViewPreview() {
    EmptyReloadView(errorMessage = "Error", modifier = Modifier.fillMaxSize(), reloadClick = {})
}
