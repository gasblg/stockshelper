package com.github.gasblg.stockshelper.shares.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import com.github.gasblg.stockshelper.models.SharesModel
import com.github.gasblg.stockshelper.models.enums.Market
import com.github.gasblg.stockshelper.models.enums.Type
import com.github.gasblg.stockshelper.shares.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoBottomSheet(
    scope: CoroutineScope,
    model: SharesModel?,
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
            text = model?.ticker ?: "",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .padding(start = 16.dp),
            text = model?.shortName ?: "",
            fontSize = 12.sp,
        )
        Text(
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
            text = model?.name ?: "",
            fontSize = 14.sp,
        )
        Text(
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
            text = model?.description ?: "",
            fontSize = 14.sp,
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
                    stringResource(id = R.string.features_shares_hide),
                    color = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun BottomSheetPreview() {
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

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    InfoBottomSheet(
        scope = scope,
        model = sharesModel,
        sheetState = sheetState,
        showBottomSheet = {

        }
    )
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
