package com.example.a4_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.a4_2.ui.theme._4_2Theme

// One stroke = its points + the width it was drawn with
data class PenStroke(val points: List<Offset>, val width: Float)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _4_2Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DrawingScreen()
                }
            }
        }
    }
}

@Composable
fun DrawingScreen() {
    val strokes = remember { mutableStateListOf<PenStroke>() }
    var brushSize by remember { mutableStateOf(8f) }

    Column(modifier = Modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                // Long press clears everything
                .pointerInput(Unit) {
                    detectTapGestures(onLongPress = { strokes.clear() })
                }
                // Drag = new stroke per finger-down
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { start ->
                            strokes.add(PenStroke(listOf(start), brushSize))
                        },
                        onDrag = { change, _ ->
                            val last = strokes.last()
                            strokes[strokes.lastIndex] = last.copy(points = last.points + change.position)
                            change.consume()
                        }
                    )
                }
        ) {
            strokes.forEach { drawStroke(it.points, it.width) }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text("Brush size: ${brushSize.toInt()} px")
            Slider(
                value = brushSize,
                onValueChange = { brushSize = it },
                valueRange = 2f..60f
            )
            Text("Long-press anywhere on the canvas to clear.")
        }
    }
}

private fun DrawScope.drawStroke(points: List<Offset>, width: Float) {
    if (points.size < 2) return
    val path = Path().apply {
        moveTo(points.first().x, points.first().y)
        points.drop(1).forEach { lineTo(it.x, it.y) }
    }
    drawPath(
        path = path,
        color = Color.Black,
        style = Stroke(width, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
}
