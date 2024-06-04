package com.github.gasblg.stockshelper.news.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.gasblg.stockshelper.design.component.EmptyReloadView
import com.github.gasblg.stockshelper.design.icon.StocksIcons
import com.github.gasblg.stockshelper.models.NewsModel
import com.github.gasblg.stockshelper.news.R
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun DetailRoute(
    newsId: Int,
    viewModel: DetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state = viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true, block = {
        newsId.let {
            viewModel.load(it)
        }
    })

    DetailScreen(
        state,
        newsId,
        onBackClick,
        viewModel::load
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun DetailScreen(
    state: State<DetailViewModel.UiState>,
    newsId: Int,
    onBackClick: () -> Unit,
    reloadClick: (Int) -> Unit
) {

    val progressState = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            modifier = Modifier.background(color = Color.Green),
            title = {
                Text(text = stringResource(id = R.string.features_news_news))
            },
            navigationIcon = {
                IconButton(onClick = { onBackClick.invoke() }) {
                    Icon(StocksIcons.Back, contentDescription = null)
                }
            },
        )
        if (progressState.value) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }


        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (state.value) {
                is DetailViewModel.UiState.Success -> {

                    progressState.value = false
                    val news = (state.value as DetailViewModel.UiState.Success).news

                    val title = news.title
                    val date = news.date
                    val body = news.body ?: ""

                    HtmlText(title, date, body)
                }

                is DetailViewModel.UiState.Loading -> {
                    progressState.value = true
                }

                is DetailViewModel.UiState.Error -> {
                    progressState.value = false
                    val errorMessage =
                        (state.value as DetailViewModel.UiState.Error).error.message ?: ""
                    EmptyReloadView(
                        errorMessage = errorMessage,
                        modifier = Modifier.fillMaxSize(),
                        reloadClick = {
                            reloadClick.invoke(newsId)
                        })
                }
            }
        }
    }
}


@Composable
fun HtmlText(title: String, date: String, body: String) {
    val convertedTitle = HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_COMPACT).toString()
    val convertedBody = HtmlCompat.fromHtml(body, HtmlCompat.FROM_HTML_MODE_COMPACT).toString()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
                .padding(8.dp),

            verticalArrangement = Arrangement.Center,

            ) {
            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = convertedTitle,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 24.sp
            )

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = date,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 10.sp
            )

            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = convertedBody,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 12.sp
            )
        }
    }

}

@SuppressLint("StateFlowValueCalledInComposition")
@Preview
@Composable
fun DetailScreenPreview(
) {
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
    val uiState: MutableStateFlow<DetailViewModel.UiState> = MutableStateFlow(
        DetailViewModel.UiState.Loading
    )
    uiState.value = DetailViewModel.UiState.Success(newsModel)

    val newsState = uiState.collectAsState()

    DetailScreen(
        state = newsState,
        newsId = 897,
        onBackClick = {},
        reloadClick = {}
    )
}

@Preview
@Composable
private fun EmptyViewPreview() {
    EmptyReloadView(errorMessage = "Error", modifier = Modifier.fillMaxSize(), reloadClick = {})
}