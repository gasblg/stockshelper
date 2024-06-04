package com.github.gasblg.stockshelper.news

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.core.text.HtmlCompat
import com.github.gasblg.stockshelper.models.NewsModel


@Composable
fun NewsItem(
    model: NewsModel?,
    onNewsClick: (Int) -> Unit
) {
    NewsItemView(model = model, onNewsClick)
}


@Composable
private fun NewsItemView(model: NewsModel?, onNewsClick: (Int) -> Unit) {

    val convertedTitle =
        HtmlCompat.fromHtml(model?.title ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT).toString()
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier

            .fillMaxWidth()
            .padding(top = 4.dp, start = 8.dp, end = 8.dp)


    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    enabled = true,
                    onClick = { model?.id?.let { onNewsClick.invoke(it) } }
                )
        )
        {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
            )
            {
                Column(
                    modifier = Modifier
                        .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                        .height(70.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        modifier = Modifier.weight(7f),
                        text = convertedTitle,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        modifier = Modifier.weight(3f),
                        text = model?.date ?: "",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 10.sp,
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
private fun NewsItemViewPreview() {
    val newsModel = NewsModel(
        id = 14,
        date = "2024-03-22",
        title = "Japanese Yen Hovers Near 4-Month Lows",
        body = "The Japanese yen held around 151.5 per dollar, hovering near its lowest levels in four months as the dollar strengthened on bets that US Federal Reserve monetary policy could remain restrictive even as other major central banks start cutting interest rates." + "<br>" +
                "Domestically, investors digested data showing Japan’s headline and core inflation rates both rose to a four-month high of 2.8% in February, supporting the Bank of Japan’s decision to end negative rates." + "<br>" +
                "Earlier this week, the BOJ raised rates from -0.1% to 0%, hiking for the first time since 2007 and ending eight years of negative rates amid rising wages and high inflation." + "<br>" +
                "The central bank also abandoned its yield curve control policy, ended ETF and J-REIT purchases and reduced bond buying activities." + "<br>" +
                "However, BOJ Governor Kazuo Ueda said the central bank will maintain an accommodative stance for some time, keeping rates at 0%.",
    )

    NewsItemView(model = newsModel, onNewsClick = {})
}
