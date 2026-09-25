package com.muhammadsalman.draw.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class ToolMode { None, Brush, Eraser }

@Composable
fun BottomTools(
    visible: Boolean,
    toolMode: ToolMode,
    onToolModeChange: (ToolMode) -> Unit,
    brushSize: Float,
    onBrushSizeChange: (Float) -> Unit,
    eraserSize: Float,
    onEraserSizeChange: (Float) -> Unit,
    brushColors: List<Color?>,
    selectedBrushColor: Color,
    onBrushColorSelect: (Color) -> Unit,
    onBrushColorEdit: (Int, Color) -> Unit,
    onBrushColorAdd: (Int, Color) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!visible) return

    Surface(
        color = Color(0xFFF3F4F6),
        shadowElevation = 8.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            // Row 1: tool modes
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                FilterChip(
                    selected = toolMode == ToolMode.Brush,
                    onClick = { onToolModeChange(if (toolMode == ToolMode.Brush) ToolMode.None else ToolMode.Brush) },
                    label = { Text("Brush") },
                    leadingIcon = {
                        Icon(Icons.Default.Brush, contentDescription = null)
                    }
                )
                FilterChip(
                    selected = toolMode == ToolMode.Eraser,
                    onClick = { onToolModeChange(if (toolMode == ToolMode.Eraser) ToolMode.None else ToolMode.Eraser) },
                    label = { Text("Eraser") },
                    leadingIcon = {
                        Icon(Icons.Default.Clear, contentDescription = null)
                    }
                )
            }

            Spacer(Modifier.height(8.dp))

            // Row 2: brush size slider
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Brush", modifier = Modifier.width(60.dp))
                Slider(
                    value = brushSize,
                    onValueChange = onBrushSizeChange,
                    valueRange = 5f..80f,
                    modifier = Modifier.weight(1f)
                )
                Text("${brushSize.toInt()}px", modifier = Modifier.width(48.dp))
            }

            // Row 3: eraser size slider
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Eraser", modifier = Modifier.width(60.dp))
                Slider(
                    value = eraserSize,
                    onValueChange = onEraserSizeChange,
                    valueRange = 5f..80f,
                    modifier = Modifier.weight(1f)
                )
                Text("${eraserSize.toInt()}px", modifier = Modifier.width(48.dp))
            }

            Spacer(Modifier.height(6.dp))

            // Row 4: brush color palette
            PaletteRow(
                colors = brushColors,
                selectedColor = selectedBrushColor,
                onSelect = onBrushColorSelect,
                onEdit = onBrushColorEdit,
                onAdd = onBrushColorAdd,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
