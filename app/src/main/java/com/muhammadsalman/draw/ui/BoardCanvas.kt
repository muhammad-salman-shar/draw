package com.muhammadsalman.draw.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun BoardCanvas(
    boardColor: Color,
    gridEnabled: Boolean,
    gridSize: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(boardColor)
    ) {
        if (gridEnabled) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val step = gridSize
                if (step <= 0f) return@Canvas

                val gridColor = Color(0x33000000)
                val strokePx = 1.dp.toPx()

                var x = 0f
                while (x <= size.width) {
                    drawLine(
                        color = gridColor,
                        start = Offset(x, 0f),
                        end = Offset(x, size.height),
                        strokeWidth = strokePx
                    )
                    x += step
                }

                var y = 0f
                while (y <= size.height) {
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = strokePx
                    )
                    y += step
                }
            }
        }
    }
}
