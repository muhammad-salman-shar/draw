package com.muhammadsalman.draw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.weight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.muhammadsalman.draw.ui.BoardCanvas
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
    var gridEnabled by remember { mutableStateOf(true) }
    var gridSize by remember { mutableFloatStateOf(40f) }
    var isLocked by remember { mutableStateOf(false) }
    var boardColor by remember { mutableStateOf(Color.White) }

    // 3 preset + 3 custom slots (null = empty, "+")
    val boardColors = remember {
        mutableStateListOf<Color?>(
            Color(0xFFEF4444), // red
            Color(0xFF3B82F6), // blue
            Color(0xFF111827), // black
            null,
            null,
            null
        )
    }

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
            onBoardColorSelect = { boardColor = it }
        )

        BoardCanvas(
            boardColor = boardColor,
            gridEnabled = gridEnabled,
            gridSize = gridSize,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        )
    }
}
