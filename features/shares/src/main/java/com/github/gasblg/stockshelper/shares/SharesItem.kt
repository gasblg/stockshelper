package com.github.gasblg.stockshelper.shares

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
import com.github.gasblg.stockshelper.common.ext.percentColor
import com.github.gasblg.stockshelper.common.ext.percentText
import com.github.gasblg.stockshelper.models.SharesModel
import com.github.gasblg.stockshelper.models.enums.Market
import com.github.gasblg.stockshelper.models.enums.Type


@Composable
fun SharesItem(
    model: SharesModel,
    onItemClick: (SharesModel) -> Unit
) {

    SharesItemView(model = model, onItemClick)

}

@Composable
private fun SharesItemView(model: SharesModel, onItemClick: (SharesModel) -> Unit) {

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, start = 8.dp, end = 8.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .clickable(
                enabled = true,
                onClick = {
                    onItemClick.invoke(model)
                }
            )

            .padding(8.dp)

        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(7f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = model.ticker,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis

                    )

                    Text(
                        text = model.shortName,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Column(
                    modifier = Modifier.weight(3f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = model.price.toString(),
                        color = MaterialTheme.colorScheme.onSurface,

                        )
                    Text(
                        text = model.percent.percentText(),
                        color = model.percent.percentColor()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SharesItemViewPreview() {
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
    SharesItemView(model = sharesModel, onItemClick = {})
}