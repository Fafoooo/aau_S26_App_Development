package com.example.a3_3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a3_3.ui.theme._3_3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _3_3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    UnitConverterScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun UnitConverterScreen(modifier: Modifier = Modifier) {
    val conversions = listOf("Meter -> Inch", "Celsius -> Fahrenheit", "Kg -> Pounds")
    var selectedConversion by remember { mutableStateOf(conversions[0]) }
    var expanded by remember { mutableStateOf(false) }
    var inputValue by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Unit Converter",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // DropdownMenu to select conversion mode
        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text(selectedConversion)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                conversions.forEach { conversion ->
                    DropdownMenuItem(
                        text = { Text(conversion) },
                        onClick = {
                            selectedConversion = conversion
                            expanded = false
                            result = ""
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input field
        TextField(
            value = inputValue,
            onValueChange = { inputValue = it },
            label = { Text("Enter value") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Convert button
        Button(onClick = {
            val input = inputValue.toDoubleOrNull()
            result = if (input == null) {
                "Invalid input"
            } else {
                when (selectedConversion) {
                    "Meter -> Inch" -> "%.2f inches".format(input * 39.3701)
                    "Celsius -> Fahrenheit" -> "%.2f F".format(input * 9.0 / 5.0 + 32.0)
                    "Kg -> Pounds" -> "%.2f lbs".format(input * 2.20462)
                    else -> "Unknown"
                }
            }
        }) {
            Text("Convert")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Result display
        if (result.isNotEmpty()) {
            Text(
                text = result,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConverterPreview() {
    _3_3Theme {
        UnitConverterScreen()
    }
}
