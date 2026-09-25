package com.muhammadsalman.draw.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridOff
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopBar(
    gridEnabled: Boolean,
    onGridToggle: (Boolean) -> Unit,
    gridSize: Float,
    onGridSizeChange: (Float) -> Unit,
    isLocked: Boolean,
    onLockToggle: (Boolean) -> Unit,
    boardColors: List<Color?>,
    selectedBoardColor: Color,
    onBoardColorSelect: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFFF3F4F6),
        shadowElevation = 4.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { onGridToggle(!gridEnabled) }) {
                    Icon(
                        imageVector = if (gridEnabled) Icons.Default.GridOn else Icons.Default.GridOff,
                        contentDescription = "Toggle grid",
                        tint = if (gridEnabled) Color(0xFF3B82F6) else Color(0xFF6B7280)
                    )
                }
                Slider(
                    value = gridSize,
                    onValueChange = onGridSizeChange,
                    valueRange = 10f..100f,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { onLockToggle(!isLocked) }) {
                    Icon(
                        imageVector = if (isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                        contentDescription = "Lock board",
                        tint = if (isLocked) Color(0xFFEF4444) else Color(0xFF6B7280)
                    )
                }
            }

            Spacer(Modifier.height(6.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                boardColors.forEach { color ->
                    BoardColorChip(
                        color = color,
                        isSelected = color != null && color == selectedBoardColor,
                        onClick = { if (color != null) onBoardColorSelect(color) }
                    )
                }
            }
        }
    }
}

@Composable
private fun BoardColorChip(
    color: Color?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
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
            .clickable(onClick = onClick),
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
