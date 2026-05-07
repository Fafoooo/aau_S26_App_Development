package com.example.a4_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.a4_1.ui.theme._4_1Theme
import kotlin.math.hypot
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _4_1Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TouchCanvasScreen()
                }
            }
        }
    }
}

@Composable
fun TouchCanvasScreen() {
    var redPoint by remember { mutableStateOf<Offset?>(null) }
    var bluePoint by remember { mutableStateOf<Offset?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    // First time: place red somewhere random
                    if (redPoint == null) redPoint = randomOffset(size.width, size.height)

                    detectTapGestures(
                        onTap = { bluePoint = it },
                        onDoubleTap = { redPoint = randomOffset(size.width, size.height) },
                        onLongPress = { redPoint = it }
                    )
                }
        ) {
            val red = redPoint ?: return@Canvas
            drawCircle(Color.Red, 40f, red)

            bluePoint?.let { blue ->
                drawLine(Color.Black, red, blue, strokeWidth = 4f)
                drawCircle(Color.Blue, 40f, blue)
            }
        }

        val red = redPoint
        val blue = bluePoint
        val distanceText = if (red != null && blue != null)
            /** normale distanzformel*/
            "Distance: %.1f px".format(hypot(blue.x - red.x, blue.y - red.y))
        else
            "Tap somewhere to set the blue point"

        Text(
            text = distanceText,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 32.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

private fun randomOffset(maxWidth: Int, maxHeight: Int): Offset =
    Offset(Random.nextFloat() * maxWidth, Random.nextFloat() * maxHeight)
