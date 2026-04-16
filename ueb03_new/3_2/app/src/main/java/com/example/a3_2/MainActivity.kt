package com.example.a3_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a3_2.ui.theme._3_2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _3_2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ModifierDemo(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ModifierDemo(modifier: Modifier = Modifier) {
    var buttonClicked by remember { mutableStateOf(false) }
    var boxClicked by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Styled button with rounded corners, custom color, disabled after one click
        Button(
            onClick = { buttonClicked = true },
            enabled = !buttonClicked,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = Color.Gray
            ),
            modifier = Modifier.padding(8.dp)
        ) {
            Text(if (buttonClicked) "Already Clicked" else "Click Me Once")
        }

        // Text with larger font, custom color and background
        Text(
            text = "Styled Text",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Clickable box with dashed border that changes color
        val borderColor = if (boxClicked) Color.Green else Color.Red
        Box(
            modifier = Modifier
                .size(120.dp)
                .drawBehind {
                    drawRoundRect(
                        color = borderColor,
                        cornerRadius = CornerRadius(12.dp.toPx()),
                        style = Stroke(
                            width = 3.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(
                                floatArrayOf(10f, 10f), 0f
                            )
                        )
                    )
                }
                .clip(RoundedCornerShape(12.dp))
                .clickable { boxClicked = !boxClicked },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (boxClicked) "ON" else "OFF",
                color = borderColor,
                fontSize = 18.sp
            )
        }

        // Layout control: fillMaxWidth with alignment
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Left-aligned (fillMaxWidth)",
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        // Layout control: wrapContentSize
        Box(
            modifier = Modifier
                .wrapContentSize()
                .background(Color.LightGray, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text("wrapContentSize")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ModifierPreview() {
    _3_2Theme {
        ModifierDemo()
    }
}
