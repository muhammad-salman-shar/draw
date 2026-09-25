package com.muhammadsalman.draw.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

/**
 * Ek Stroke = ek drawing gesture (finger down -> drag -> finger up).
 * Har stroke apna color + width + points rakhta hai (per-stroke object model).
 */
data class Stroke(
    val points: List<Offset>,
    val color: Color,
    val widthPx: Float,
    val isEraser: Boolean = false
)
