package com.muhammadsalman.draw.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke as DrawStroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.muhammadsalman.draw.model.Stroke

@Composable
fun BoardCanvas(
    boardColor: Color,
    gridEnabled: Boolean,
    gridSize: Float,
    strokes: List<Stroke>,
    toolMode: ToolMode,
    brushColor: Color,
    brushSizePx: Float,
    eraserSizePx: Float,
    isLocked: Boolean,
    onStrokeComplete: (Stroke) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentPoints by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var currentColor by remember { mutableStateOf(Color.Black) }
    var currentWidth by remember { mutableStateOf(8f) }
    var currentIsEraser by remember { mutableStateOf(false) }

    val drawingEnabled = !isLocked && toolMode != ToolMode.None

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(boardColor)
            .then(
                if (drawingEnabled) {
                    Modifier.pointerInput(toolMode, brushColor, brushSizePx, eraserSizePx) {
                        awaitEachGesture {
                            val down = awaitFirstDown(requireUnconsumed = false)
                            currentIsEraser = toolMode == ToolMode.Eraser
                            currentColor = if (currentIsEraser) boardColor else brushColor
                            currentWidth = if (currentIsEraser) eraserSizePx else brushSizePx
                            currentPoints = listOf(down.position)
                            down.consume()

                            var pointerId = down.id
                            while (true) {
                                val event = awaitPointerEvent()
                                val change = event.changes.firstOrNull { it.id == pointerId }
                                    ?: event.changes.firstOrNull()
                                    ?: break
                                if (!change.pressed) {
                                    break
                                }
                                currentPoints = currentPoints + change.position
                                change.consume()
                            }

                            if (currentPoints.isNotEmpty()) {
                                onStrokeComplete(
                                    Stroke(
                                        points = currentPoints,
                                        color = currentColor,
                                        widthPx = currentWidth,
                                        isEraser = currentIsEraser
                                    )
                                )
                            }
                            currentPoints = emptyList()
                        }
                    }
                } else Modifier
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // 1. Grid
            if (gridEnabled) {
                val step = gridSize
                if (step > 0f) {
                    val gridColor = Color(0x33000000)
                    val strokePx = 1.dp.toPx()
                    var x = 0f
                    while (x <= size.width) {
                        drawLine(gridColor, Offset(x, 0f), Offset(x, size.height), strokePx)
                        x += step
                    }
                    var y = 0f
                    while (y <= size.height) {
                        drawLine(gridColor, Offset(0f, y), Offset(size.width, y), strokePx)
                        y += step
                    }
                }
            }

            // 2. Completed strokes
            strokes.forEach { stroke ->
                drawOneStroke(stroke)
            }

            // 3. Current live stroke
            if (currentPoints.isNotEmpty()) {
                drawOneStroke(
                    Stroke(
                        points = currentPoints,
                        color = currentColor,
                        widthPx = currentWidth,
                        isEraser = currentIsEraser
                    )
                )
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawOneStroke(stroke: Stroke) {
    if (stroke.points.isEmpty()) return
    val cap = StrokeCap.Round
    if (stroke.points.size == 1) {
        drawCircle(
            color = stroke.color,
            radius = stroke.widthPx / 2f,
            center = stroke.points[0]
        )
        return
    }
    for (i in 0 until stroke.points.size - 1) {
        drawLine(
            color = stroke.color,
            start = stroke.points[i],
            end = stroke.points[i + 1],
            strokeWidth = stroke.widthPx,
            cap = cap
        )
    }
}
