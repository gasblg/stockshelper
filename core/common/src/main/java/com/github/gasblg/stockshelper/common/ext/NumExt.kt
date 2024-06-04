package com.github.gasblg.stockshelper.common.ext

import androidx.compose.ui.graphics.Color
import com.github.gasblg.stockshelper.design.theme.GreenColor
import com.github.gasblg.stockshelper.design.theme.RedColor

fun Double.percentColor() = run {
    if (this > 0.0) {
        GreenColor
    } else if (this < 0.0) {
        RedColor
    } else {
        Color.Gray
    }
}

fun Double.percentText() = run {
    if ((this) > 0.0) {
        "+${this}%"
    } else {
        "${this}%"
    }
}
