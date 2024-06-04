package com.github.gasblg.stockshelper.currencies.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.gasblg.stockshelper.currencies.firstBaselineToTop
import com.github.gasblg.stockshelper.common.ext.percentColor
import com.github.gasblg.stockshelper.common.ext.percentText
import com.github.gasblg.stockshelper.currencies.R
import com.github.gasblg.stockshelper.design.component.EmptyReloadView
import com.github.gasblg.stockshelper.design.component.Item
import com.github.gasblg.stockshelper.design.icon.StocksIcons
import com.github.gasblg.stockshelper.models.CurrencyModel
import com.github.gasblg.stockshelper.models.enums.Market
import com.github.gasblg.stockshelper.models.enums.Type


@Composable
fun CurrencyRoute(
    ticker: String?,
    onBackClick: () -> Unit,
    infoViewModel: CurrencyViewModel = hiltViewModel()
) {
    CurrencyScreen(
        ticker = ticker,
        infoViewModel,
        onBackClick,
        infoViewModel::load
    )
}

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencyScreen(
    ticker: String?,
    viewModel: CurrencyViewModel,
    onBackClick: () -> Unit,
    reloadClick: (String) -> Unit
) {
    val state = viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true, block = {
        viewModel.load(ticker)
    })
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
            title = {
                Text(text = stringResource(id = R.string.features_currencies_currencies))
            },
            navigationIcon = {
                IconButton(onClick = { onBackClick.invoke() }) {
                    Icon(StocksIcons.Back, contentDescription = null)
                }
            },
        )
        if (progressState.value) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            when (state.value) {
                is CurrencyViewModel.UiState.Success -> {
                    progressState.value = false
                    val currencyModel =
                        (state.value as CurrencyViewModel.UiState.Success).currencyModel

                    CurrencyInfo(currencyModel)
                }

                is CurrencyViewModel.UiState.Loading -> {
                    progressState.value = true
                }

                is CurrencyViewModel.UiState.Error -> {
                    progressState.value = false

                    val errorMessage =
                        (state.value as CurrencyViewModel.UiState.Error).error.message ?: ""
                    EmptyReloadView(
                        errorMessage = errorMessage,
                        modifier = Modifier.fillMaxSize(),
                        reloadClick = { reloadClick.invoke(ticker ?: "") })
                }
            }
        }
    }
}

@Composable
fun CurrencyInfo(model: CurrencyModel) {
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
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Text(
                    text = model.ticker,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                    text = model.description,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp, bottom = 20.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Top,
                ) {
                    Text(
                        modifier = Modifier.firstBaselineToTop(80.dp),
                        text = stringResource(
                            id = R.string.features_currencies_rub,
                            model.roundedRate().toString()
                        ),
                        fontSize = 48.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        modifier = Modifier
                            .firstBaselineToTop(80.dp)
                            .padding(start = 8.dp),
                        text = model.percent.percentText(),
                        color = model.percent.percentColor(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Item(
                    stringResource(id = R.string.features_currencies_date_short),
                    model.date
                )
                Item(
                    stringResource(id = R.string.features_currencies_type),
                    stringResource(id = Type.getType(model.type))
                )
                Item(
                    stringResource(id = R.string.features_currencies_market),
                    stringResource(id = Market.getName(model.market))
                )
            }
        }
    }
}

@Preview
@Composable
private fun ItemPreview() {
    val currencyModel = CurrencyModel(
        ticker = "CAD/RUB",
        name = "CAD - Canadian Dollar",
        shortName = "Canadian Dollar",
        description = "Canadian Dollar to Russian ruble",
        type = Type.CURRENCY.value,
        date = "2024-03-22",
        market = Market.CURRENCY.value,
        rate = 67.479,
        percent = -0.56
    )
    CurrencyInfo(currencyModel)
}

