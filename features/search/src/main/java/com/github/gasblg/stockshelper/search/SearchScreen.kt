package com.github.gasblg.stockshelper.search

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData

import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.gasblg.stockshelper.design.component.BottomProgressView
import com.github.gasblg.stockshelper.design.component.EmptyView
import com.github.gasblg.stockshelper.design.icon.StocksIcons
import com.github.gasblg.stockshelper.models.SearchModel
import com.github.gasblg.stockshelper.models.enums.Market
import com.github.gasblg.stockshelper.models.enums.Type
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
internal fun SearchRoute(
    onBackClick: () -> Unit,
    onTopicClick: (SearchModel?) -> Unit,
    searchViewModel: SearchViewModel = hiltViewModel(),
) {
    val searchQuery by searchViewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResultUiState: LazyPagingItems<SearchModel> =
        searchViewModel.uiState.collectAsLazyPagingItems()

    SearchScreen(
        onBackClick = onBackClick,
        onSearchQueryChanged = searchViewModel::onSearchQueryChanged,
        onSearchTriggered = searchViewModel::onSearchTriggered,
        onTopicClick = onTopicClick,
        onClearRecentSearches = searchViewModel::clearRecentSearches,
        searchQuery = searchQuery,
        state = searchResultUiState
    )
}

@Composable
internal fun SearchScreen(
    onBackClick: () -> Unit = {},
    onSearchQueryChanged: (String) -> Unit = {},
    onSearchTriggered: (String) -> Unit = {},
    onTopicClick: (SearchModel?) -> Unit = {},
    onClearRecentSearches: () -> Unit = {},
    searchQuery: String,
    state: LazyPagingItems<SearchModel>
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        SearchToolbar(
            onBackClick = onBackClick,
            onSearchQueryChanged = onSearchQueryChanged,
            onSearchTriggered = onSearchTriggered,
            searchQuery = searchQuery,
            onClearRecentSearches = onClearRecentSearches
        )

        state.apply {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 4.dp, bottom = 8.dp)
            ) {

                items(itemCount) { index ->
                    SearchItem(
                        model = state[index],
                        onTopicClick
                    )
                }

                when (loadState.refresh) {
                    is LoadState.Loading -> {}

                    is LoadState.Error -> {
                        item {
                            val errorMessage =
                                (loadState.refresh as LoadState.Error).error.message ?: ""
                            EmptyView(
                                errorMessage = errorMessage,
                                modifier = Modifier.fillParentMaxSize()
                            )
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


@Composable
private fun SearchToolbar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    searchQuery: String,
    onSearchTriggered: (String) -> Unit,
    onClearRecentSearches: () -> Unit = {}

) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),

        ) {
        IconButton(onClick = { onBackClick() }) {
            Icon(
                imageVector = StocksIcons.Back,
                contentDescription = null
            )
        }
        SearchTextField(
            onSearchQueryChanged = onSearchQueryChanged,
            onSearchTriggered = onSearchTriggered,
            searchQuery = searchQuery,
            onClearRecentSearches = onClearRecentSearches
        )
    }
}

@Composable
private fun SearchTextField(
    onSearchQueryChanged: (String) -> Unit,
    searchQuery: String,
    onSearchTriggered: (String) -> Unit,
    onClearRecentSearches: () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val onSearchExplicitlyTriggered = {
        keyboardController?.hide()
        onSearchTriggered(searchQuery)
    }

    TextField(
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        leadingIcon = {
            Icon(
                imageVector = StocksIcons.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onSearchQueryChanged("")
                        onClearRecentSearches.invoke()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        onValueChange = {
            if (!it.contains("\n")) {
                onSearchQueryChanged(it)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .focusRequester(focusRequester)
            .onKeyEvent {
                if (it.key == Key.Enter) {
                    onSearchExplicitlyTriggered()
                    true
                } else {
                    false
                }
            }
            .testTag("searchTextField"),
        shape = RoundedCornerShape(32.dp),
        value = searchQuery,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchExplicitlyTriggered()
            },
        ),
        maxLines = 1,
        singleLine = true,
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Preview
@Composable
fun SearchScreenPreview() {
    val searchModel = SearchModel(
        "MTLR",
        "Public Joint Stock Company \"Mechel\"",
        "Mechel",
        "Mechel Group - metallurgical, electricity generating and transport company. The group includes about 20 industrial enterprises: producers of coal, iron ore, steel, rolled products, ferroalloys, thermal and electrical energy.",
        Type.SHARES.value,
        Market.STOCK.value
    )

    val uiState: MutableStateFlow<PagingData<SearchModel>> =
        MutableStateFlow(value = PagingData.empty())
    uiState.value = PagingData.from(listOf(searchModel))
    val searchState = uiState.collectAsLazyPagingItems()

    SearchScreen(
        onBackClick = {},
        onSearchQueryChanged = {},
        onSearchTriggered = {},
        onTopicClick = {},
        searchQuery = "query",
        state = searchState
    )
}

@Preview
@Composable
private fun EmptyViewPreview() {
    EmptyView(errorMessage = "Error", modifier = Modifier.fillMaxSize())
}
