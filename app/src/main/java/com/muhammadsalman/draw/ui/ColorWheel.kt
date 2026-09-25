package com.muhammadsalman.draw.ui

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

@Composable
fun ColorWheelDialog(
    initialColor: Color,
    onSave: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    val hsv = remember {
        FloatArray(3).also { AndroidColor.colorToHSV(initialColor.toArgb(), it) }
    }
    var hue by remember { mutableFloatStateOf(hsv[0]) }
    var saturation by remember { mutableFloatStateOf(hsv[1]) }
    var value by remember { mutableFloatStateOf(hsv[2]) }

    val currentColor = Color(
        AndroidColor.HSVToColor(floatArrayOf(hue, saturation, value))
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Pick a color")

                Spacer(Modifier.height(16.dp))

                Box(
                    modifier = Modifier.size(220.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(
                        modifier = Modifier
                            .size(220.dp)
                            .pointerInput(Unit) {
                                detectTapGestures { offset ->
                                    val c = Offset(size.width / 2f, size.height / 2f)
                                    val r = minOf(size.width, size.height).toFloat() / 2f
                                    val dx = offset.x - c.x
                                    val dy = offset.y - c.y
                                    val dist = hypot(dx, dy)
                                    saturation = (dist / r).coerceIn(0f, 1f)
                                    var a = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
                                    if (a < 0f) a += 360f
                                    hue = a
                                }
                            }
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragStart = { offset ->
                                        val c = Offset(size.width / 2f, size.height / 2f)
                                        val r = minOf(size.width, size.height).toFloat() / 2f
                                        val dx = offset.x - c.x
                                        val dy = offset.y - c.y
                                        val dist = hypot(dx, dy)
                                        saturation = (dist / r).coerceIn(0f, 1f)
                                        var a = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
                                        if (a < 0f) a += 360f
                                        hue = a
                                    },
                                    onDrag = { change, _ ->
                                        val c = Offset(size.width / 2f, size.height / 2f)
                                        val r = minOf(size.width, size.height).toFloat() / 2f
                                        val dx = change.position.x - c.x
                                        val dy = change.position.y - c.y
                                        val dist = hypot(dx, dy)
                                        saturation = (dist / r).coerceIn(0f, 1f)
                                        var a = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
                                        if (a < 0f) a += 360f
                                        hue = a
                                        change.consume()
                                    }
                                )
                            }
                    ) {
                        val radius = minOf(size.width, size.height).toFloat() / 2f
                        val center = Offset(size.width / 2f, size.height / 2f)

                        drawCircle(
                            brush = Brush.sweepGradient(
                                listOf(
                                    Color.Red,
                                    Color.Yellow,
                                    Color.Green,
                                    Color.Cyan,
                                    Color.Blue,
                                    Color.Magenta,
                                    Color.Red
                                )
                            ),
                            radius = radius,
                            center = center
                        )

                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(Color.White, Color.Transparent),
                                center = center,
                                radius = radius
                            ),
                            radius = radius,
                            center = center
                        )

                        val angleRad = Math.toRadians(hue.toDouble())
                        val markerX = center.x + cos(angleRad).toFloat() * saturation * radius
                        val markerY = center.y + sin(angleRad).toFloat() * saturation * radius

                        drawCircle(
                            color = Color.Black,
                            radius = 9.dp.toPx(),
                            center = Offset(markerX, markerY),
                            style = Stroke(width = 3.dp.toPx())
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 6.dp.toPx(),
                            center = Offset(markerX, markerY),
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text("Brightness")
                Slider(
                    value = value,
                    onValueChange = { value = it },
                    valueRange = 0f..1f,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(currentColor)
                    )
                    Spacer(Modifier.size(12.dp))
                    Text("#%06X".format(currentColor.toArgb() and 0xFFFFFF))
                }

                Spacer(Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) { Text("Close") }

                    Button(
                        onClick = { onSave(currentColor) },
                        modifier = Modifier.weight(1f)
                    ) { Text("Save") }
                }
            }
        }
    }
}
