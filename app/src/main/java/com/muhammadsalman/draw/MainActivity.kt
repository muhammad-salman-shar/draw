package com.muhammadsalman.draw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.muhammadsalman.draw.model.Stroke
import com.muhammadsalman.draw.ui.BoardCanvas
import com.muhammadsalman.draw.ui.BottomTools
import com.muhammadsalman.draw.ui.ToolMode
import com.muhammadsalman.draw.ui.TopBar
import com.muhammadsalman.draw.ui.theme.DrawTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DrawTheme {
                DrawApp()
            }
        }
    }
}

@Composable
fun DrawApp() {
    // Board state
    var gridEnabled by remember { mutableStateOf(true) }
    var gridSize by remember { mutableFloatStateOf(40f) }
    var isLocked by remember { mutableStateOf(false) }
    var boardColor by remember { mutableStateOf(Color.White) }

    val boardColors = remember {
        mutableStateListOf<Color?>(
            Color(0xFFEF4444),
            Color(0xFF3B82F6),
            Color(0xFF111827),
            null,
            null,
            null
        )
    }

    // Strokes
    val strokes = remember { mutableStateListOf<Stroke>() }

    // Tools state
    var toolsVisible by remember { mutableStateOf(false) }
    var toolMode by remember { mutableStateOf(ToolMode.None) }
    var brushSizeDp by remember { mutableFloatStateOf(12f) }
    var eraserSizeDp by remember { mutableFloatStateOf(24f) }
    var brushColor by remember { mutableStateOf(Color(0xFF111827)) }

    val brushColors = remember {
        mutableStateListOf<Color?>(
            Color(0xFF111827),
            Color(0xFFEF4444),
            Color(0xFFFFFFFF),
            null,
            null,
            null
        )
    }

    val density = androidx.compose.ui.platform.LocalDensity.current
    val brushSizePx = with(density) { brushSizeDp.dp.toPx() }
    val eraserSizePx = with(density) { eraserSizeDp.dp.toPx() }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(
                gridEnabled = gridEnabled,
                onGridToggle = { gridEnabled = it },
                gridSize = gridSize,
                onGridSizeChange = { gridSize = it },
                isLocked = isLocked,
                onLockToggle = { isLocked = it },
                boardColors = boardColors,
                selectedBoardColor = boardColor,
                onBoardColorSelect = { boardColor = it },
                onBoardColorEdit = { idx, c -> boardColors[idx] = c },
                onBoardColorAdd = { idx, c -> boardColors[idx] = c }
            )

            BoardCanvas(
                boardColor = boardColor,
                gridEnabled = gridEnabled,
                gridSize = gridSize,
                strokes = strokes,
                toolMode = toolMode,
                brushColor = brushColor,
                brushSizePx = brushSizePx,
                eraserSizePx = eraserSizePx,
                isLocked = isLocked,
                onStrokeComplete = { strokes.add(it) },
                onErase = { newList ->
                    strokes.clear()
                    strokes.addAll(newList)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )

            BottomTools(
                visible = toolsVisible,
                toolMode = toolMode,
                onToolModeChange = { toolMode = it },
                brushSize = brushSizeDp,
                onBrushSizeChange = { brushSizeDp = it },
                eraserSize = eraserSizeDp,
                onEraserSizeChange = { eraserSizeDp = it },
                brushColors = brushColors,
                selectedBrushColor = brushColor,
                onBrushColorSelect = { brushColor = it },
                onBrushColorEdit = { idx, c -> brushColors[idx] = c },
                onBrushColorAdd = { idx, c -> brushColors[idx] = c }
            )
        }

        if (toolsVisible) {
            SmallFloatingActionButton(
                onClick = { toolsVisible = false },
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 210.dp)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close tools")
            }
        } else {
            FloatingActionButton(
                onClick = { toolsVisible = true },
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
            ) {
                Icon(Icons.Default.Build, contentDescription = "Tools")
            }
        }
    }
}
