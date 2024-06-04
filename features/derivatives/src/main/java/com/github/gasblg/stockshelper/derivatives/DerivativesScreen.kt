package com.github.gasblg.stockshelper.derivatives

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.gasblg.stockshelper.design.component.EmptyReloadView
import com.github.gasblg.stockshelper.design.component.Item
import com.github.gasblg.stockshelper.design.icon.StocksIcons
import com.github.gasblg.stockshelper.models.DerivativesModel
import com.github.gasblg.stockshelper.models.enums.Market
import com.github.gasblg.stockshelper.models.enums.Type
import kotlinx.coroutines.flow.MutableStateFlow


@Composable
fun SearchInfoScreen(
    ticker: String?,
    onBackClick: () -> Unit,
    viewModel: DerivativesViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true, block = {
        viewModel.load(ticker)
    })

    ItemView(
        ticker = ticker,
        state,
        onBackClick,
        viewModel::load
    )
}

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemView(
    ticker: String?,
    state: State<DerivativesViewModel.UiState>,
    onBackClick: () -> Unit,
    reloadClick: (String) -> Unit
) {

    val progressState = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            title = {
                Text(text = stringResource(id = R.string.features_derivatives_derivatives))
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
            contentAlignment = Alignment.TopCenter
        ) {
            when (state.value) {
                is DerivativesViewModel.UiState.Success -> {
                    progressState.value = false
                    val infoModel =
                        (state.value as DerivativesViewModel.UiState.Success).derivatives


                    Card(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background)
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.background)

                            ) {
                                item {
                                    Item(
                                        stringResource(id = R.string.features_derivatives_ticker),
                                        infoModel.ticker
                                    )
                                    Item(
                                        stringResource(id = R.string.features_derivatives_short_name),
                                        infoModel.shortName
                                    )
                                    Item(
                                        stringResource(id = R.string.features_derivatives_name),
                                        infoModel.name
                                    )
                                    Item(
                                        stringResource(id = R.string.features_derivatives_description),
                                        infoModel.description
                                    )
                                    Item(
                                        stringResource(id = R.string.features_derivatives_first_trade),
                                        infoModel.firstTrade
                                    )
                                    Item(
                                        stringResource(id = R.string.features_derivatives_last_short),
                                        infoModel.lastTrade
                                    )
                                    Item(
                                        stringResource(id = R.string.features_derivatives_date_short),
                                        infoModel.date
                                    )
                                    Item(
                                        stringResource(id = R.string.features_derivatives_contract_type),
                                        infoModel.contractType
                                    )
                                    Item(
                                        stringResource(id = R.string.features_derivatives_type),
                                        stringResource(id = Type.getType(infoModel.type))
                                    )
                                    Item(
                                        stringResource(id = R.string.features_derivatives_market),
                                        stringResource(id = Market.getName(infoModel.market))
                                    )
                                }
                            }
                        }
                    }
                }

                is DerivativesViewModel.UiState.Loading -> {
                    progressState.value = true
                }

                is DerivativesViewModel.UiState.Error -> {
                    progressState.value = false

                    val errorMessage =
                        (state.value as DerivativesViewModel.UiState.Error).error.message ?: ""
                    EmptyReloadView(
                        errorMessage = errorMessage,
                        modifier = Modifier.fillMaxSize(),
                        reloadClick = { reloadClick.invoke(ticker ?: "") })
                }
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Preview
@Composable
private fun ItemViewPreview() {
    val model = DerivativesModel(
        ticker = "RGBI",
        name = "Futures contract RGBI",
        shortName = "RGBI",
        description = "RGBI Index futures contract",
        type = "futures",
        market = "derivatives",
        firstTrade = "2023-11-20",
        lastTrade = "2024-06-03",
        date = "2024-06-03",
        contractType = "Cash Settled"
    )
    val uiState: MutableStateFlow<DerivativesViewModel.UiState> = MutableStateFlow(
        DerivativesViewModel.UiState.Loading
    )
    uiState.value = DerivativesViewModel.UiState.Success(model)

    val state = uiState.collectAsState()

    ItemView(ticker = "RGBI", state = state, onBackClick = {}, reloadClick = {})
}



