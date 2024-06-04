package com.github.gasblg.stockshelper.shares.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.gasblg.stockshelper.common.ext.percentColor
import com.github.gasblg.stockshelper.common.ext.percentText
import com.github.gasblg.stockshelper.design.component.EmptyView
import com.github.gasblg.stockshelper.design.icon.StocksIcons
import com.github.gasblg.stockshelper.design.theme.RedColor
import com.github.gasblg.stockshelper.models.CandleModel
import com.github.gasblg.stockshelper.models.SharesModel
import com.github.gasblg.stockshelper.models.enums.Market
import com.github.gasblg.stockshelper.models.enums.Type
import com.github.gasblg.stockshelper.shares.R
import com.github.gasblg.stockshelper.shares.chart.ChartType
import com.github.gasblg.stockshelper.shares.chart.ChartView


@Composable
fun SharesDetailRoute(
    ticker: String?,
    viewModel: SharesDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    SharesDetailScreen(
        ticker,
        viewModel,
        onBackClick,
        checkExisting = viewModel::checkExisting
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SharesDetailScreen(
    ticker: String?,
    viewModel: SharesDetailViewModel,
    onBackClick: () -> Unit,
    checkExisting: (ticker: String) -> Unit,
) {
    val state = viewModel.uiState.collectAsState()
    val existingState = viewModel.existingState.collectAsState()
    val progressState = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = true, block = {
        ticker?.let {
            viewModel.observeExisting(it)
            viewModel.start(it)
        }
    })
    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.onSecondary
            ),
            title = {
            },
            navigationIcon = {
                IconButton(onClick = { onBackClick.invoke() }) {
                    Icon(StocksIcons.Back, contentDescription = null)
                }
            },

            )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (progressState.value) {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            when (state.value) {
                is SharesDetailViewModel.UiState.Success -> {
                    progressState.value = false

                    val sharesModel =
                        (state.value as SharesDetailViewModel.UiState.Success).sharesModel
                    val candles = sharesModel.candles ?: listOf()

                    val list =
                        candles.map {
                            CandleModel(
                                viewModel.getDay(it.date),
                                price = it.price,
                                open = it.open,
                                low = it.low,
                                high = it.high,
                                close = it.close,
                                volume = it.volume,
                                percent = it.percent
                            )
                        }

                    ChartFullView(
                        sharesModel = sharesModel,
                        list,
                        checkExisting,
                        existingState,
                        viewModel.getDayOrYesterday(sharesModel.date)
                    )
                }


                is SharesDetailViewModel.UiState.Loading -> {
                    progressState.value = true
                }


                is SharesDetailViewModel.UiState.Error -> {
                    progressState.value = false

                    val errorMessage =
                        (state.value as SharesDetailViewModel.UiState.Error).error.message ?: ""

                    EmptyView(errorMessage = errorMessage, modifier = Modifier.fillMaxSize())
                }
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun ChartFullView(
    sharesModel: SharesModel,
    candles: List<CandleModel>,
    checkExisting: (ticker: String) -> Unit,
    existingState: State<Boolean>,
    currentDate: String
) {
    val sheetState = rememberModalBottomSheetState()

    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()

    var bottomSheetContent: (@Composable () -> Unit)? by remember {
        mutableStateOf(null)
    }

    val scroll = rememberScrollState(0)

    val icon = when (existingState.value) {
        true -> StocksIcons.Check
        else -> StocksIcons.Add
    }
    val open = sharesModel.open
    val close = sharesModel.close
    val low = sharesModel.low
    val high = sharesModel.high

    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier.heightIn(min = 250.dp),
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState,
            content = {
                Box(
                    modifier = Modifier.defaultMinSize(minHeight = 1.dp)
                ) {
                    bottomSheetContent?.let { it() }
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopStart,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scroll)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(6f),
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    Text(
                        modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                        text = sharesModel.ticker,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Icon(
                        imageVector = StocksIcons.Info,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 4.dp, top = 16.dp, end = 16.dp)
                            .clickable(
                                onClick = {
                                    bottomSheetContent = {
                                        InfoBottomSheet(
                                            scope = scope,
                                            model = sharesModel,
                                            sheetState = sheetState,
                                            showBottomSheet = {
                                                showBottomSheet = false
                                            }
                                        )
                                    }
                                    showBottomSheet = true
                                }
                            ),
                        tint = MaterialTheme.colorScheme.onSurface
                    )

                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.BottomEnd,
                ) {


                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 16.dp, end = 16.dp)
                            .clickable(
                                onClick = {
                                    sharesModel.ticker.let { checkExisting.invoke(it) }
                                }
                            ),
                        tint = MaterialTheme.colorScheme.onSurface

                    )
                }
            }


            Text(
                modifier = Modifier.padding(top = 4.dp, start = 16.dp),
                text = sharesModel.shortName,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 12.sp
            )

            Text(
                modifier = Modifier.padding(top = 4.dp, start = 16.dp),
                text = stringResource(id = R.string.features_shares_rub, sharesModel.price),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
            )

            Row {
                Text(
                    modifier = Modifier.padding(top = 4.dp, start = 16.dp),
                    text = sharesModel.percent.percentText(),
                    color = sharesModel.percent.percentColor()
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp, start = 4.dp),
                    text = currentDate,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            Column(modifier = Modifier.padding(start = 16.dp, top = 8.dp)) {
                Row {
                    Text(
                        modifier = Modifier.padding(end = 4.dp),
                        text = stringResource(id = R.string.features_shares_open, open),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 10.sp
                    )
                    Text(
                        text = stringResource(id = R.string.features_shares_close, close),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 10.sp
                    )
                }
                Row {
                    Text(
                        modifier = Modifier.padding(end = 4.dp),
                        text = stringResource(id = R.string.features_shares_low, low),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 10.sp
                    )
                    Text(
                        text = stringResource(id = R.string.features_shares_high, high),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 10.sp
                    )
                }
            }


            ChartView(
                candles.reversed().take(30).reversed(),
                sharesModel.percent,
                ChartType.CANDLES
            )

            Text(
                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                text = stringResource(id = R.string.features_shares_open_with),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp
            )

            Row(
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp)
                    .fillMaxWidth()
            ) {
                TradingViewButton(sharesModel.ticker)
                MoexButton(id = sharesModel.ticker)
            }
        }
    }
}

@Composable
fun TradingViewButton(id: String) {
    val context = LocalContext.current
    val intent = remember {
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.tradingview.com/chart/?symbol=$id")
        )
    }

    Button(
        modifier = Modifier.padding(top = 8.dp, start = 16.dp),
        onClick = { context.startActivity(intent) },
        border = BorderStroke(1.dp, Color.White),

        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.White
        ),

        ) {
        Text(text = "TradingView")
    }
}

@Composable
fun MoexButton(id: String) {
    val context = LocalContext.current
    val intent = remember {
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.moex.com/ru/issue.aspx?board=TQBR&code=$id")
        )
    }

    Button(
        modifier = Modifier.padding(top = 8.dp, start = 16.dp),
        border = BorderStroke(1.dp, RedColor),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = RedColor
        ),
        onClick = { context.startActivity(intent) }) {
        Text(text = "MOEX")
    }
}


@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun ChartFullViewPreview() {

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

    val candles = listOf(
        CandleModel( "03.01", 311.03f, 311.03f, 326.74f, 326.74f, 12429476, 326.74f, 5.06f),
    CandleModel( "04.01", 327.68f, 323.1f, 329.39f, 326.9f, 10175769, 326.9f, 0.05f),
    CandleModel( "05.01", 328.0f, 325.0f, 328.49f, 328.46f, 3683364, 328.46f, 0.48f),
    CandleModel( "08.01", 328.99f, 323.57f, 334.28f, 326.37f, 12827944, 326.37f, -0.64f),
    CandleModel( "09.01", 328.0f, 324.22f, 333.54f, 332.88f, 8029246, 332.88f, 1.99f),
    CandleModel( "10.01", 333.0f, 324.33f, 333.71f, 325.4f, 8127257, 325.4f, -2.25f),
    CandleModel( "11.01", 325.4f, 318.6f, 326.9f, 322.35f, 11022840, 322.35f, -0.94f),
    CandleModel( "12.01", 322.4f, 315.6f, 323.75f, 318.2f, 8599879, 318.2f, -1.29f),
    CandleModel( "15.01", 318.3f, 314.52f, 319.9f, 316.0f, 5982028, 316.0f, -0.69f),
    CandleModel( "16.01", 316.0f, 313.6f, 323.59f, 322.36f, 8954924, 322.36f, 2.01f),
    CandleModel( "17.01", 322.29f, 319.42f, 326.43f, 320.29f, 8666004, 320.29f, -0.64f),
    CandleModel( "18.01", 321.0f, 317.11f, 322.3f, 318.19f, 5097134, 318.19f, -0.66f),
    CandleModel( "19.01", 317.85f, 315.04f, 319.0f, 318.48f, 5422062, 318.48f, 0.09f),
    CandleModel( "22.01", 318.8f, 310.57f, 319.68f, 310.87f, 8687752, 310.87f, -2.39f),
    CandleModel( "23.01", 309.9f, 304.1f, 309.9f, 305.96f, 10851938, 305.96f, -1.58f),
    CandleModel( "24.01", 305.61f, 295.66f, 309.74f, 297.81f, 18313629, 297.81f, -2.66f),
    CandleModel( "25.01", 296.03f, 291.6f, 304.67f, 303.89f, 14311419, 303.89f, 2.04f),
    CandleModel( "26.01", 305.0f, 300.8f, 306.78f, 301.78f, 7423228, 301.78f, -0.69f),
    CandleModel( "29.01", 302.53f, 297.3f, 303.59f, 298.29f, 7106627, 298.29f, -1.16f),
    CandleModel( "30.01", 298.0f, 295.66f, 314.85f, 314.48f, 18093408, 314.48f, 5.43f),
    CandleModel( "31.01", 315.32f, 314.5f, 322.42f, 319.88f, 21217586, 319.88f, 1.72f),
    CandleModel( "01.02", 320.05f, 315.7f, 322.35f, 317.61f, 9210603, 317.61f, -0.71f),
    CandleModel( "02.02", 317.55f, 313.4f, 320.8f, 318.69f, 9288524, 318.69f, 0.34f),
    CandleModel( "05.02", 319.5f, 319.0f, 329.0f, 328.77f, 15639794, 328.77f, 3.16f),
    CandleModel( "06.02", 329.2f, 322.1f, 329.57f, 323.24f, 9452357, 323.24f, -1.68f),
    CandleModel( "07.02", 323.0f, 319.2f, 325.99f, 321.4f, 7875779, 321.4f, -0.57f),
    CandleModel( "08.02", 321.99f, 317.15f, 325.79f, 317.94f, 10551723, 317.94f, -1.08f),
    CandleModel( "09.02", 316.8f, 312.0f, 318.24f, 315.1f, 9594124, 315.1f, -0.89f),
    CandleModel( "12.02", 315.5f, 312.67f, 319.29f, 315.39f, 5428371, 315.39f, 0.09f),
    CandleModel( "13.02", 315.86f, 314.6f, 319.9f, 319.17f, 5185453, 319.17f, 1.2f),
    CandleModel( "14.02", 319.0f, 317.01f, 321.35f, 317.55f, 4518086, 317.55f, -0.51f),
    CandleModel( "15.02", 317.45f, 313.17f, 317.45f, 314.89f, 5557489, 314.89f, -0.84f),
    CandleModel( "16.02", 315.3f, 309.5f, 316.2f, 310.51f, 6607610, 310.51f, -1.39f),
    CandleModel( "19.02", 311.2f, 308.76f, 315.38f, 313.09f, 5714779, 313.09f, 0.83f),
    CandleModel( "20.02", 313.3f, 305.06f, 316.5f, 305.39f, 14076314, 305.39f, -2.46f),
    CandleModel( "21.02", 305.38f, 276.33f, 307.94f, 281.76f, 33668319, 281.76f, -7.74f),
    CandleModel( "22.02", 282.5f, 270.5f, 285.67f, 276.71f, 31704688, 276.71f, -1.79f),
    CandleModel( "26.02", 266.6f, 262.4f, 280.47f, 279.91f, 20999449, 279.91f, 1.16f),
    CandleModel( "27.02", 281.0f, 278.21f, 288.69f, 281.65f, 18988454, 281.65f, 0.62f),
    CandleModel( "28.02", 281.41f, 277.0f, 282.98f, 278.58f, 9307189, 278.58f, -1.09f),
    CandleModel( "29.02", 278.75f, 270.31f, 278.8f, 271.44f, 10233086, 271.44f, -2.56f),
    CandleModel( "01.03", 272.0f, 271.16f, 279.74f, 278.95f, 9307660, 278.95f, 2.77f),
    CandleModel( "04.03", 281.1f, 279.67f, 287.48f, 285.99f, 12694442, 285.99f, 2.52f),
    CandleModel( "05.03", 287.8f, 283.21f, 288.0f, 284.27f, 5210273, 284.27f, -0.6f),
    CandleModel( "06.03", 284.4f, 281.35f, 284.75f, 282.1f, 4604824, 282.1f, -0.76f),
    CandleModel( "07.03", 281.86f, 280.12f, 283.88f, 281.4f, 4396680, 281.4f, -0.25f),
    CandleModel( "11.03", 282.0f, 282.0f, 291.99f, 290.19f, 7390710, 290.19f, 3.12f),
    CandleModel( "12.03", 290.28f, 288.05f, 303.49f, 300.39f, 22396075, 300.39f, 3.51f),
    CandleModel( "13.03", 301.0f, 293.07f, 303.99f, 294.63f, 14833754, 294.63f, -1.92f),
    CandleModel( "14.03", 294.64f, 289.21f, 297.28f, 295.0f, 13109233, 295.0f, 0.13f),
    CandleModel( "15.03", 295.0f, 291.1f, 297.71f, 292.83f, 5730223, 292.83f, -0.74f),
    CandleModel( "18.03", 293.8f, 288.2f, 294.77f, 290.87f, 5498568, 290.87f, -0.67f),
    CandleModel( "19.03", 290.6f, 289.55f, 297.8f, 293.97f, 11654057, 293.97f, 1.07f),
    CandleModel( "20.03", 294.92f, 285.12f, 296.31f, 287.01f, 9607268, 287.01f, -2.37f),
    CandleModel( "21.03", 287.01f, 283.02f, 289.09f, 287.51f, 6979008, 287.51f, 0.17f),
    CandleModel( "22.03", 287.6f, 276.24f, 288.9f, 279.62f, 7931445, 279.62f, -2.74f)
    )

    val list =
        candles.map {
            CandleModel(
                date = it.date,
                open = it.open,
                low = it.low,
                high = it.high,
                close = it.close,
                volume = it.volume,
                price = it.price,
                percent = it.percent
            )
        }

    ChartFullView(
        sharesModel = sharesModel,
        candles = list,
        existingState = mutableStateOf(true),
        checkExisting = {},
        currentDate = "22.03"
    )

}

@Preview
@Composable
private fun EmptyViewPreview() {
    EmptyView(errorMessage = "Error", modifier = Modifier.fillMaxSize())
}

