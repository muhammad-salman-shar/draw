package com.muhammadsalman.draw.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Row of color chips with 3 behaviors:
 *  - Tap filled chip  -> apply color (onSelect)
 *  - Long-press chip  -> open color wheel to edit that slot's color (onEdit)
 *  - Tap "+" (null)   -> open color wheel to add new color into that empty slot (onAdd)
 */
@Composable
fun PaletteRow(
    colors: List<Color?>,
    selectedColor: Color,
    onSelect: (Color) -> Unit,
    onEdit: (Int, Color) -> Unit,
    onAdd: (Int, Color) -> Unit,
    modifier: Modifier = Modifier
) {
    var editingIndex by remember { mutableStateOf<Int?>(null) }
    var editingInitial by remember { mutableStateOf(Color.White) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        colors.forEachIndexed { index, color ->
            val isSelected = color != null && color == selectedColor

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color ?: Color(0xFFE5E7EB))
                    .border(
                        width = if (isSelected) 3.dp else 1.dp,
                        color = if (isSelected) Color(0xFF3B82F6) else Color(0xFFD1D5DB),
                        shape = CircleShape
                    )
                    .pointerInput(index, color) {
                        detectTapGestures(
                            onTap = {
                                if (color != null) onSelect(color)
                                else {
                                    editingInitial = Color.White
                                    editingIndex = index
                                }
                            },
                            onLongPress = {
                                if (color != null) {
                                    editingInitial = color
                                    editingIndex = index
                                }
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                if (color == null) {
                    Text(
                        text = "+",
                        color = Color(0xFF6B7280),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    editingIndex?.let { idx ->
        val hadColor = colors.getOrNull(idx) != null
        ColorWheelDialog(
            initialColor = editingInitial,
            onSave = { newColor ->
                if (hadColor) onEdit(idx, newColor) else onAdd(idx, newColor)
                editingIndex = null
            },
            onDismiss = { editingIndex = null }
        )
    }
}
