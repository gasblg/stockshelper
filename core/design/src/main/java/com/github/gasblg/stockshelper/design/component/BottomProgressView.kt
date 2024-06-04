package com.github.gasblg.stockshelper.design.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.gasblg.stockshelper.design.R

@Composable
fun BottomProgressView() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource( id = R.string.core_design_loading))
        CircularProgressIndicator()
    }

}

@Preview
@Composable
fun BottomProgressViewPreview() {
    BottomProgressView()
}