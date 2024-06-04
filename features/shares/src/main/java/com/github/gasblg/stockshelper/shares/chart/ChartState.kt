package com.github.gasblg.stockshelper.shares.chart

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue
import com.github.gasblg.stockshelper.models.CandleModel
import kotlin.math.roundToInt

class ChartState {

    var candles = listOf<CandleModel>()
    var chartType: ChartType = ChartType.CANDLES
    var dates by mutableStateOf(listOf<CandleModel>())

    private var viewWidth = 0f
    private var viewHeight = 0f

    private val maxPrice by derivedStateOf { candles.maxOfOrNull { it.high } ?: 0f }
    val minPrice by derivedStateOf { candles.minOfOrNull { it.low } ?: 0f }

    val prices by derivedStateOf {
        val priceItem = (maxPrice - minPrice) / PRICES_COUNT
        mutableListOf<Float>().apply {
            repeat(PRICES_COUNT + 2) {
                if (it > 0)
                    add((maxPrice + priceItem) - priceItem * it)
            }
        }
    }


    fun setViewSize(width: Float, height: Float) {
        viewWidth = width
        viewHeight = height
    }

    fun calculateGridWidth() {
        val width = viewWidth / candles.size
        val w = GRID_WIDTH / width

        dates = if (width >= GRID_WIDTH) {
            candles.drop(1)
        } else {
            candles.filterIndexed { index, _ ->
                (index + 1) % w.roundToInt() == 0
            }
        }
    }

    fun xOffset(candle: CandleModel) =
        viewWidth * candles.indexOf(candle).toFloat() / candles.size

    fun yOffset(value: Float) =
        viewHeight / 12 + (viewHeight * (((maxPrice - value)) / (maxPrice - minPrice)) * 0.5f)

    companion object {

        private const val GRID_WIDTH = 200
        private const val PRICES_COUNT = 10

        fun getState(
            candles: List<CandleModel>,
            chartType: ChartType
        ) =
            ChartState().apply {
                this.candles = candles
                this.chartType = chartType
            }

        @Suppress("UNCHECKED_CAST")
        val Saver: Saver<ChartState, Any> = listSaver(
            save = {
                listOf(
                    it.candles,
                    it.chartType
                )
            },
            restore = {
                getState(
                    candles = it[0] as List<CandleModel>,
                    chartType = it[1] as ChartType
                )
            }
        )
    }
}