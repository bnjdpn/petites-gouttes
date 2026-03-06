package com.bnjdpn.petitesgouttes.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ChartEntry(val label: String, val value: Float)

@Composable
fun BarChart(
    entries: List<ChartEntry>,
    title: String,
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.primary,
    maxLabelCount: Int = 7
) {
    if (entries.isEmpty()) return

    val maxValue = entries.maxOf { it.value }.coerceAtLeast(1f)

    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        val density = LocalDensity.current
        val textColor = MaterialTheme.colorScheme.onSurface

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val barCount = entries.size
            val bottomPadding = 40f
            val topPadding = 20f
            val chartHeight = canvasHeight - bottomPadding - topPadding
            val barWidth = (canvasWidth / barCount) * 0.6f
            val barSpacing = canvasWidth / barCount

            entries.forEachIndexed { index, entry ->
                val barHeight = if (maxValue > 0) (entry.value / maxValue) * chartHeight else 0f
                val x = index * barSpacing + (barSpacing - barWidth) / 2

                drawRect(
                    color = barColor,
                    topLeft = Offset(x, topPadding + chartHeight - barHeight),
                    size = Size(barWidth, barHeight)
                )

                if (entry.value > 0) {
                    drawContext.canvas.nativeCanvas.drawText(
                        entry.value.toInt().toString(),
                        x + barWidth / 2,
                        topPadding + chartHeight - barHeight - 4f,
                        android.graphics.Paint().apply {
                            color = textColor.hashCode()
                            textSize = with(density) { 10.sp.toPx() }
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                    )
                }
            }

            drawLine(
                color = textColor.copy(alpha = 0.3f),
                start = Offset(0f, topPadding + chartHeight),
                end = Offset(canvasWidth, topPadding + chartHeight),
                strokeWidth = 1f
            )

            val labelStep = (barCount / maxLabelCount).coerceAtLeast(1)
            entries.forEachIndexed { index, entry ->
                if (index % labelStep == 0 || index == barCount - 1) {
                    drawContext.canvas.nativeCanvas.drawText(
                        entry.label,
                        index * barSpacing + barSpacing / 2,
                        canvasHeight - 4f,
                        android.graphics.Paint().apply {
                            color = textColor.hashCode()
                            textSize = with(density) { 9.sp.toPx() }
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                    )
                }
            }
        }
    }
}
