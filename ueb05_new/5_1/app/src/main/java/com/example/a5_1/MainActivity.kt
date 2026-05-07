package com.example.a5_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.a5_1.ui.theme._5_1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _5_1Theme {
                NavApp()
            }
        }
    }
}

// "Repository" - liefert Items per ID
data class Item(val id: Int, val title: String, val description: String)

object ItemRepository {
    val items = listOf(
        Item(1, "Apple",  "A red, crunchy fruit."),
        Item(2, "Banana", "A yellow, soft fruit."),
        Item(3, "Cherry", "A small, sweet fruit."),
        Item(4, "Donut",  "A sweet, ring-shaped pastry."),
        Item(5, "Egg",    "An oval, protein-rich food.")
    )
    fun byId(id: Int) = items.firstOrNull { it.id == id }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavApp() {
    val navController = rememberNavController()
    val currentEntry by navController.currentBackStackEntryAsState()
    val route = currentEntry?.destination?.route

    // Title + back-button-Logik aus dem aktuellen Route ableiten
    val (title, canGoBack) = when {
        route == "home"                           -> "Home"     to false
        route == "settings"                       -> "Settings" to true
        route?.startsWith("detail/") == true      -> "Detail"   to true
        else                                       -> "App"      to false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    if (canGoBack) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
            composable("home") {
                HomeScreen(
                    onItemClick     = { id -> navController.navigate("detail/$id") },
                    onSettingsClick = { navController.navigate("settings") }
                )
            }
            composable(
                route = "detail/{itemId}",
                arguments = listOf(navArgument("itemId") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("itemId") ?: 0
                DetailScreen(itemId = id)
            }
            composable("settings") {
                SettingsScreen()
            }
        }
    }
}

@Composable
fun HomeScreen(onItemClick: (Int) -> Unit, onSettingsClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = onSettingsClick) {
            Text("Open Settings")
        }
        Spacer(Modifier.height(16.dp))
        Text("Items:", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            items(ItemRepository.items) { item ->
                Column(
                    modifier = Modifier
                        .clickable { onItemClick(item.id) }
                        .padding(vertical = 12.dp)
                ) {
                    Text(item.title, style = MaterialTheme.typography.bodyLarge)
                    Text("Tap for details", style = MaterialTheme.typography.bodySmall)
                }
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun DetailScreen(itemId: Int) {
    val item = ItemRepository.byId(itemId)
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (item != null) {
            Text(item.title, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            Text("ID: ${item.id}")
            Spacer(Modifier.height(8.dp))
            Text(item.description)
        } else {
            Text("Item with ID $itemId not found.")
        }
    }
}

@Composable
fun SettingsScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Text("This is the Settings screen.")
    }
}
