package com.example.a5_4

import android.os.Bundle
import android.util.Log
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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a5_4.ui.theme._5_4Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

private const val TAG = "Timer5_4"
private const val START_VALUE = 10

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _5_4Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TimerScreen()
                }
            }
        }
    }
}

@Composable
fun TimerScreen() {
    var count by remember { mutableIntStateOf(START_VALUE) }
    var isRunning by remember { mutableStateOf(false) }

    // Tick-Effect: laeuft nur, wenn isRunning == true.
    // Bei Pause wird der Effect durch den key-Wechsel cancelt.
    LaunchedEffect(isRunning) {
        if (!isRunning) return@LaunchedEffect
        if (count == START_VALUE) Log.d(TAG, "Timer started")
        while (count > 0 && isActive) {
            delay(1000)
            count--
        }
        if (count == 0) {
            Log.d(TAG, "Timer finished")
            isRunning = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "$count",
            fontSize = 96.sp,
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = {
                    // Start/Pause toggle. Bei count == 0 zuerst Reset noetig.
                    if (count > 0) isRunning = !isRunning
                }
            ) {
                Text(if (isRunning) "Pause" else "Start")
            }
            Button(
                onClick = {
                    isRunning = false
                    count = START_VALUE
                }
            ) {
                Text("Reset")
            }
        }
    }
}
