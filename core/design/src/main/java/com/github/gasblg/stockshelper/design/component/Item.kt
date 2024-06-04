package com.github.gasblg.stockshelper.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.gasblg.stockshelper.design.R
import com.github.gasblg.stockshelper.models.enums.Type

@Composable
fun Item(title: String, description: String) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 4.dp, start = 8.dp, end = 8.dp)
    ) {
        Text(
            text = title,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            modifier = Modifier
                .padding(bottom = 4.dp),
            text = description,
            color = MaterialTheme.colorScheme.onSurface
        )
        HorizontalDivider()
    }
}

@Preview
@Composable
private fun ItemPreview() {
    Item(stringResource(id = R.string.core_design_type), Type.CURRENCY.value)
}