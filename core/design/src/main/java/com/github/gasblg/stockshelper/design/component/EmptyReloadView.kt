package com.github.gasblg.stockshelper.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.gasblg.stockshelper.design.R

@Composable
fun EmptyReloadView(
    errorMessage: String,
    modifier: Modifier,
    reloadClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(color = MaterialTheme.colorScheme.onSurface, text = errorMessage)
        Button(modifier = Modifier.padding(top = 16.dp), onClick = {
            reloadClick.invoke()
        }) {
            Text(
                stringResource(id = R.string.core_design_reload),
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
private fun EmptyReloadViewPreview() {
    EmptyReloadView(
        errorMessage = "Error",
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        reloadClick = {}
    )
}