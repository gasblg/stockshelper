package com.github.gasblg.stockshelper.currencies

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.gasblg.stockshelper.common.ext.percentColor
import com.github.gasblg.stockshelper.common.ext.percentText
import com.github.gasblg.stockshelper.design.component.EmptyReloadView
import com.github.gasblg.stockshelper.design.icon.StocksIcons
import com.github.gasblg.stockshelper.models.CurrencyModel
import com.github.gasblg.stockshelper.models.enums.Market
import com.github.gasblg.stockshelper.models.enums.Type
import com.github.gasblg.stockshelper.settings.SettingsRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


@Composable
fun CurrenciesRoute(
    viewModel: CurrenciesViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState()

    CurrencyScreen(
        state = state,
        viewModel::reload
    )
}


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyScreen(
    state: State<CurrenciesViewModel.UiState>,
    reloadClick: () -> Unit
) {

    val sheetState = rememberModalBottomSheetState()

    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()

    var bottomSheetContent: (@Composable () -> Unit)? by remember {
        mutableStateOf(null)
    }

    var showSettingsDialog by rememberSaveable {
        mutableStateOf(false)
    }

    if (showSettingsDialog) {
        SettingsRoute(
            onDismiss = { showSettingsDialog = false }
        )
    }
    val progressState = remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {


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
        if (progressState.value) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                modifier = Modifier.background(color = Color.Green),
                title = {
                    Text(text = stringResource(id = R.string.features_currencies_currencies))
                },
                actions = {
                    IconButton(onClick = {
                        showSettingsDialog = true
                    }) {
                        Icon(StocksIcons.Settings, contentDescription = null)
                    }
                },
            )

            when (state.value) {

                is CurrenciesViewModel.UiState.Success -> {
                    progressState.value = false

                    LazyColumn(
                        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                    ) {

                        items((state.value as CurrenciesViewModel.UiState.Success).currency.size) { index ->
                            val currency =
                                (state.value as CurrenciesViewModel.UiState.Success).currency

                            CurrencyItem(
                                model = currency[index],
                                onNewsClick = {
                                    bottomSheetContent = {
                                        BottomSheet(
                                            scope = scope,
                                            model = it,
                                            sheetState = sheetState,
                                            showBottomSheet = {
                                                showBottomSheet = false
                                            }
                                        )
                                    }
                                    showBottomSheet = true
                                }

                            )
                        }
                    }

                }

                is CurrenciesViewModel.UiState.Error -> {
                    progressState.value = false

                    val errorMessage =
                        (state.value as CurrenciesViewModel.UiState.Error).error.message ?: ""
                    EmptyReloadView(
                        errorMessage = errorMessage,
                        modifier = Modifier.fillMaxSize(),
                        reloadClick = { reloadClick.invoke() })
                }

                else -> {
                    progressState.value = true
                }
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheet(
    scope: CoroutineScope,
    model: CurrencyModel,
    sheetState: SheetState,
    showBottomSheet: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .firstBaselineToTop(32.dp)
                .padding(top = 8.dp, start = 16.dp),
            text = model.ticker,

            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Row {

            Text(
                modifier = Modifier
                    .firstBaselineToTop(32.dp)
                    .padding(top = 8.dp, start = 16.dp),
                text = stringResource(
                    id = R.string.features_currencies_rub,
                    model.roundedRate().toString()
                ),

                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = model.percent.percentText(),
                color = model.percent.percentColor(),
                modifier = Modifier
                    .firstBaselineToTop(32.dp)
                    .padding(top = 8.dp, start = 6.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = model.description,
            modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp, end = 16.dp, top = 32.dp), Alignment.BottomEnd

        ) {
            Button(onClick = {
                scope.launch { sheetState.hide() }
                    .invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet.invoke(false)
                        }
                    }
            }) {
                Text(
                    stringResource(id = R.string.features_currencies_hide),
                    color = Color.White
                )
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Preview
@Composable
fun CurrencyScreenPreview(
) {
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

    val uiState: MutableStateFlow<CurrenciesViewModel.UiState> = MutableStateFlow(
        CurrenciesViewModel.UiState.Loading
    )

    uiState.value = CurrenciesViewModel.UiState.Success(listOf(currencyModel))
    val currencyState = uiState.collectAsState()


    CurrencyScreen(
        state = currencyState,
        reloadClick = {}
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun BottomSheetPreview() {
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
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    BottomSheet(
        scope = scope,
        model = currencyModel,
        sheetState = sheetState,
        showBottomSheet = {

        }
    )
}

@Preview
@Composable
private fun EmptyViewPreview() {
    EmptyReloadView(errorMessage = "Error", modifier = Modifier.fillMaxSize(), reloadClick = {})
}

fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp,
) = layout { measurable, constraints ->
    // Measure the composable
    val placeable = measurable.measure(constraints)

    // Check the composable has a first baseline
    check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
    val firstBaseline = placeable[FirstBaseline]

    // Height of the composable with padding - first baseline
    val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
    val height = placeable.height + placeableY
    layout(placeable.width, height) {
        // Where the composable gets placed
        placeable.placeRelative(0, placeableY)
    }
}

