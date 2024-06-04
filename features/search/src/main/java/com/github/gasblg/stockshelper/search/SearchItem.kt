package com.github.gasblg.stockshelper.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.github.gasblg.stockshelper.models.SearchModel
import com.github.gasblg.stockshelper.models.enums.Market
import com.github.gasblg.stockshelper.models.enums.Type

@Composable
fun SearchItem(
    model: SearchModel?,
    onItemClick: (SearchModel?) -> Unit
) {
    SearchItemView(model = model, onItemClick)
}

@Composable
private fun SearchItemView(model: SearchModel?, onItemClick: (SearchModel?) -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, start = 8.dp, end = 8.dp)

    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp)
            .clickable(
                enabled = true,
                onClick = {
                    onItemClick.invoke(model)
                }
            )
        )
        {
            Column {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Text(
                        text = model?.ticker ?: "",
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis

                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = model?.shortName ?: "",
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        Alignment.BottomEnd
                    ) {
                        Text(
                            text = model?.type ?: "",
                            fontSize = 10.sp
                        )

                    }

                }
            }
        }
    }
}

@Preview
@Composable
private fun SearchItemViewPreview() {
    val model = SearchModel(
        "MTLR",
        "Public Joint Stock Company \"Mechel\"",
        "Mechel",
        "Mechel Group - metallurgical, electricity generating and transport company. The group includes about 20 industrial enterprises: producers of coal, iron ore, steel, rolled products, ferroalloys, thermal and electrical energy.",
        Type.SHARES.value,
        Market.STOCK.value
    )
    SearchItemView(model = model, onItemClick = {})
}
