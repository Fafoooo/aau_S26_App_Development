package com.example.a3_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a3_1.ui.theme._3_1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _3_1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GreetingScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun GreetingScreen(modifier: Modifier = Modifier) {
    val names = listOf("User", "Alice", "Bob", "Compose")
    var currentIndex by remember { mutableIntStateOf(0) }

    // Text color changes based on the current name
    val greetingColor = when (currentIndex) {
        0 -> MaterialTheme.colorScheme.onBackground
        1 -> MaterialTheme.colorScheme.primary
        2 -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.tertiary
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Hello, ${names[currentIndex]}!",
            style = MaterialTheme.typography.headlineMedium,
            color = greetingColor
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row {
            Button(onClick = {
                currentIndex = (currentIndex + 1) % names.size
            }) {
                // Button shows the next name in the queue
                val nextName = names[(currentIndex + 1) % names.size]
                Text("Next: $nextName")
            }

            Spacer(modifier = Modifier.width(12.dp))

            OutlinedButton(onClick = { currentIndex = 0 }) {
                Text("Reset")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    _3_1Theme {
        GreetingScreen()
    }
}
