package com.github.gasblg.stockshelper.currencies

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.gasblg.stockshelper.common.ext.percentColor
import com.github.gasblg.stockshelper.common.ext.percentText
import com.github.gasblg.stockshelper.models.CurrencyModel
import com.github.gasblg.stockshelper.models.enums.Market
import com.github.gasblg.stockshelper.models.enums.Type


@Composable
fun CurrencyItem(
    model: CurrencyModel,
    onNewsClick: (CurrencyModel) -> Unit
) {
    CurrencyItemView(
        model = model,
        onNewsClick
    )
}


@Composable
private fun CurrencyItemView(model: CurrencyModel, onNewsClick: (CurrencyModel) -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, start = 8.dp, end = 8.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .clickable(
                    enabled = true,
                    onClick = {
                        onNewsClick.invoke(model)
                    }
                )
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = model.ticker,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = model.roundedRate().toString(),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = model.percent.percentText(),
                        color = model.percent.percentColor(),
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CurrencyItemViewPreview() {
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

    CurrencyItemView(model = currencyModel, onNewsClick = {})
}

