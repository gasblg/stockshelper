package com.github.gasblg.stockshelper.shares

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.gasblg.stockshelper.design.component.BottomProgressView
import com.github.gasblg.stockshelper.design.component.EmptyReloadView
import com.github.gasblg.stockshelper.design.component.ProgressView
import com.github.gasblg.stockshelper.design.icon.StocksIcons
import com.github.gasblg.stockshelper.design.theme.*
import com.github.gasblg.stockshelper.models.SharesModel
import com.github.gasblg.stockshelper.models.enums.Market
import com.github.gasblg.stockshelper.models.enums.Type
import com.github.gasblg.stockshelper.settings.SettingsRoute
import com.github.gasblg.stockshelper.sort.SortRoute
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow


@Composable
fun SharesRoute(
    isOnline: Boolean,
    onItemClick: (SharesModel?) -> Unit,
    onNavigationClick: () -> Unit = {},
    viewModel: SharesViewModel = hiltViewModel(),
) {
    SharesScreen(
        isOnline = isOnline,
        onItemClick = onItemClick,
        viewModel = viewModel,
        onNavigationClick = onNavigationClick,
        viewModel::reload
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharesScreen(
    isOnline: Boolean,
    onItemClick: (SharesModel?) -> Unit,
    viewModel: SharesViewModel,
    onNavigationClick: () -> Unit,
    reloadClick: () -> Unit
) {
    val state: LazyPagingItems<SharesModel> = viewModel.uiState.collectAsLazyPagingItems()
    val date = state.itemSnapshotList.firstOrNull()?.date

    val favoritesState: LazyPagingItems<SharesModel> =
        viewModel.favoritesState.collectAsLazyPagingItems()

    var showSortDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var showSettingsDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val history = stringResource(id = R.string.features_shares_history)
    val favorites = stringResource(id = R.string.features_shares_favorites)

    val items = remember {
        listOf(history, favorites)
    }

    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    if (showSortDialog) {
        SortRoute(
            onDismiss = { showSortDialog = false }
        )
    }

    if (showSettingsDialog) {
        SettingsRoute(
            onDismiss = { showSettingsDialog = false }
        )
    }
    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            modifier = Modifier.background(color = Color.Green),
            title = {
                Text(text = stringResource(id = R.string.features_shares_app_name))
            },
            navigationIcon = {
                IconButton(onClick = {
                    onNavigationClick.invoke()
                }) {
                    Icon(
                        imageVector = StocksIcons.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            },

            actions = {
                IconButton(onClick = {
                    showSortDialog = true
                }) {
                    Icon(StocksIcons.Sort, contentDescription = null)
                }
                IconButton(onClick = {
                    showSettingsDialog = true
                }) {
                    Icon(StocksIcons.Settings, contentDescription = null)
                }

            },

            )

        TextSwitch(
            selectedIndex = selectedIndex,
            items = items,
            onSelectionChange = {
                selectedIndex = it
            }
        )
        InfoView(date, isOnline)

        when (selectedIndex) {
            0 -> HistoryList(
                state,
                onItemClick,
                reloadClick = { reloadClick.invoke() }
            )

            1 -> HistoryList(
                favoritesState,
                onItemClick,
                reloadClick = { reloadClick.invoke() }
            )
        }
    }
}

@Composable
private fun HistoryList(
    state: LazyPagingItems<SharesModel>,
    onItemClick: (SharesModel?) -> Unit,
    reloadClick: () -> Unit
) {

    state.apply {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(top = 4.dp, bottom = 8.dp)
        ) {

            items(itemCount) { index ->
                state[index]?.let {
                    SharesItem(
                        model = it,
                        onItemClick
                    )
                }
            }

            when (loadState.refresh) {
                is LoadState.Loading -> {
                    item {
                        if (itemCount == 0)
                            ProgressView(Modifier.fillParentMaxSize())
                    }
                }

                is LoadState.Error -> {
                    item {
                        val errorMessage =
                            (loadState.refresh as LoadState.Error).error.message ?: ""
                        if (itemCount == 0)
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

@Composable
private fun TextSwitch(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    items: List<String>,
    onSelectionChange: (Int) -> Unit
) {
    val textColor = MaterialTheme.colorScheme.onSurface
    val surfaceColor = MaterialTheme.colorScheme.surface

    BoxWithConstraints(
        modifier
            .padding(8.dp)
            .height(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp)
    ) {
        if (items.isNotEmpty()) {

            val maxWidth = this.maxWidth
            val tabWidth = maxWidth / items.size

            val indicatorOffset by animateDpAsState(
                targetValue = tabWidth * selectedIndex,
                animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
                label = "indicator offset"
            )

            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset)
                    .shadow(4.dp, RoundedCornerShape(8.dp))
                    .width(tabWidth)
                    .fillMaxHeight()
            )


            Row(modifier = Modifier
                .fillMaxWidth()
                .drawWithContent {
                    val padding = 8.dp.toPx()
                    drawRoundRect(
                        topLeft = Offset(x = indicatorOffset.toPx() + padding, padding),
                        size = Size(size.width / 2 - padding * 2, size.height - padding * 2),
                        color = textColor,
                        cornerRadius = CornerRadius(x = 8.dp.toPx(), y = 8.dp.toPx()),
                    )

                    drawWithLayer {
                        drawContent()
                        drawRoundRect(
                            topLeft = Offset(x = indicatorOffset.toPx(), 0f),
                            size = Size(size.width / 2, size.height),
                            color = surfaceColor,
                            cornerRadius = CornerRadius(x = 8.dp.toPx(), y = 8.dp.toPx()),
                            blendMode = BlendMode.SrcOut
                        )
                    }

                }
            ) {
                items.forEachIndexed { index, text ->
                    Box(
                        modifier = Modifier
                            .width(tabWidth)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = remember {
                                    MutableInteractionSource()
                                },
                                indication = null,
                                onClick = {
                                    onSelectionChange(index)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = text,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

fun ContentDrawScope.drawWithLayer(block: ContentDrawScope.() -> Unit) {
    with(drawContext.canvas.nativeCanvas) {
        val checkPoint = saveLayer(null, null)
        block()
        restoreToCount(checkPoint)
    }
}


@Preview
@Composable
private fun TextSwitchPreview() {
    val history = stringResource(id = R.string.features_shares_history)
    val saved = stringResource(id = R.string.features_shares_favorites)

    val items = remember {
        listOf(history, saved)
    }

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }


    Column {
        TextSwitch(
            selectedIndex = selectedIndex,
            items = items,
            onSelectionChange = {
                selectedIndex = it
            }
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun InfoView(
    date: String?, isOnline: Boolean,
) {

    Row(
        Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
    ) {

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 4.dp, start = 16.dp, bottom = 4.dp),
            Alignment.BottomStart,
        ) {
            if (date != null) {
                Text(
                    text = stringResource(id = R.string.features_shares_date, date),
                    fontSize = 12.sp
                )
            }
        }
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 4.dp, start = 16.dp, bottom = 4.dp, end = 16.dp),
            Alignment.BottomEnd,
        ) {
            var show by remember { mutableStateOf(true) }

            LaunchedEffect(isOnline) {
                show = true
                delay(5000)
                show = false
            }

            val networkState =
                if (isOnline) R.string.features_shares_online else R.string.features_shares_offline
            val textColor = if (isOnline) GreenColor else RedColor

            if (show) {
                Text(
                    text = stringResource(id = networkState),
                    fontSize = 12.sp,
                    color = textColor
                )
            }
        }
    }
}


@Preview
@Composable
private fun InfoViewPreview() {
    val date = "22.03.2024"
    InfoView(date, true)
}


@SuppressLint("StateFlowValueCalledInComposition")
@Preview
@Composable
private fun HistoryListPreview() {
    val sharesModel = SharesModel(
        "MTLR",
        "2024-03-22",
        "Public Joint Stock Company \"Mechel\"",
        "Mechel",
        "Mechel Group - metallurgical, electricity generating and transport company. The group includes about 20 industrial enterprises: producers of coal, iron ore, steel, rolled products, ferroalloys, thermal and electrical energy.",
        Type.SHARES.value,
        Market.STOCK.value,
        279.62,
        287.6,
        276.24,
        288.9,
        279.62,
        7931445,
        -2.74
    )
    val uiState: MutableStateFlow<PagingData<SharesModel>> =
        MutableStateFlow(value = PagingData.empty())
    uiState.value = PagingData.from(listOf(sharesModel))
    val sharesState = uiState.collectAsLazyPagingItems()

    HistoryList(
        state = sharesState,
        onItemClick = {},
        reloadClick = {}
    )
}

@Preview
@Composable
private fun EmptyViewPreview() {
    EmptyReloadView(errorMessage = "Error", modifier = Modifier.fillMaxSize(), reloadClick = {})
}