// =====================================================================
// 5_2 = 5_1 + ModalNavigationDrawer + Deep Links (Web, Map, PDF)
//
// Inhaltlich identisch zu 5_1 (gleiche 3 Screens, gleiche 5 Items),
// erweitert um:
//   - einen seitlichen ModalNavigationDrawer mit Navigation + Deep Links
//   - 3 Deep-Link-Aktionen (Web/Map/PDF) per Android Intent
//   - Menu-Icon in der TopAppBar zum Öffnen des Drawers
// =====================================================================
package com.example.a5_2

import android.content.Intent                                       // NEU für Deep Links
import android.net.Uri                                              // NEU für Deep Links
import android.os.Bundle
import android.widget.Toast                                         // NEU für Fehlermeldungen
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
import androidx.compose.material.icons.filled.Menu                  // NEU: Hamburger-Icon
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue                       // NEU für Drawer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet                  // NEU für Drawer
import androidx.compose.material3.ModalNavigationDrawer             // NEU für Drawer
import androidx.compose.material3.NavigationDrawerItem              // NEU für Drawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState               // NEU für Drawer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope              // NEU für Drawer (open/close async)
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext                    // NEU für Intents
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider                           // NEU für PDF-Sharing
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.a5_2.ui.theme._5_2Theme
import kotlinx.coroutines.launch                                    // NEU für Drawer
import java.io.File                                                 // NEU für PDF-Datei

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _5_2Theme {
                NavApp()
            }
        }
    }
}

// === IDENTISCH zu 5_1: Datenmodell + Repository ===
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
    // === IDENTISCH zu 5_1 ===
    val navController = rememberNavController()
    val currentEntry by navController.currentBackStackEntryAsState()
    val route = currentEntry?.destination?.route

    val (title, canGoBack) = when {
        route == "home"                       -> "Home"     to false
        route == "settings"                   -> "Settings" to true
        route?.startsWith("detail/") == true  -> "Detail"   to true
        else                                  -> "App"      to false
    }

    // === NEU: Drawer-State + Coroutine-Scope + Context ===
    // drawerState steuert offen/geschlossen, scope für async-Aufrufe
    // (drawerState.open()/close() sind suspend-Funktionen, brauchen einen Scope)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current  // Man greift auf Android-Sachen zu.

    // === NEU: ModalNavigationDrawer wrappt das ganze Scaffold ===
    // Dadurch ist der Drawer von JEDEM Screen aus per Wischen oder
    // Menu-Icon erreichbar.
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))

                // --- Section 1: Screen-Navigation ---
                Text(
                    "Navigation",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                NavigationDrawerItem(
                    label = { Text("Home") },
                    selected = route == "home",
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("home") { launchSingleTop = true }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Detail (Item 1)") },
                    selected = route?.startsWith("detail/") == true,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("detail/1") { launchSingleTop = true }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Settings") },
                    selected = route == "settings",
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("settings") { launchSingleTop = true }
                    }
                )

                // --- Trennlinie zwischen normaler Nav und Deep Links ---
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // --- Section 2: Deep Links (öffnen externe Apps) ---
                Text(
                    "Deep Links",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                NavigationDrawerItem(
                    label = { Text("Open Web Page") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        openWebLink(context, "https://www.aau.at/")
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Open Map (Klagenfurt)") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        openMapLink(context, lat = 46.6228, lng = 14.3055, label = "Klagenfurt")
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Open local PDF") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        openPdfLink(context)
                    }
                )
            }
        }
    ) {
        // === IDENTISCH zu 5_1, mit EINEM Unterschied im navigationIcon ===
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(title) },
                    navigationIcon = {
                        if (canGoBack) {
                            // Wie in 5_1: Back-Button auf Detail/Settings
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        } else {
                            // NEU: Auf Home statt nichts zeigen wir das Hamburger-Menu
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Open drawer")
                            }
                        }
                    }
                )
            }
        ) { padding ->
            // === IDENTISCH zu 5_1: NavHost mit 3 Screens ===
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
}

// === IDENTISCH zu 5_1: HomeScreen, DetailScreen, SettingsScreen ===

@Composable
fun HomeScreen(onItemClick: (Int) -> Unit, onSettingsClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = onSettingsClick) { Text("Open Settings") }
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

// =====================================================================
// NEU in 5_2: Deep-Link Helper-Funktionen
// Jede öffnet via Intent eine externe App (Browser, Map, PDF-Reader).
// =====================================================================

private fun openWebLink(context: android.content.Context, url: String) {
    // Intent mit ACTION_VIEW + http(s)-URI -> Browser öffnet die Seite.
    runCatching {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }.onFailure {
        Toast.makeText(context, "No browser installed.", Toast.LENGTH_SHORT).show()
    }
}

private fun openMapLink(context: android.content.Context, lat: Double, lng: Double, label: String) {
    // geo:lat,lng?q=lat,lng(Label) ist die Standard-Map-URI auf Android.
    // Google Maps oder andere installierte Map-Apps nehmen das.
    val uri = Uri.parse("geo:$lat,$lng?q=$lat,$lng(${Uri.encode(label)})")
    runCatching {
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }.onFailure {
        Toast.makeText(context, "No map app installed.", Toast.LENGTH_SHORT).show()
    }
}

private fun openPdfLink(context: android.content.Context) {
    // Erwartet sample.pdf in app/src/main/assets/. Wird beim ersten Klick
    // nach filesDir/documents/ kopiert und über FileProvider als content://
    // URI an einen PDF-Reader übergeben (Sicherheits-Anforderung ab Android 7).
    try {
        val targetDir = File(context.filesDir, "documents").apply { mkdirs() }
        val targetFile = File(targetDir, "sample.pdf")
        if (!targetFile.exists()) {
            context.assets.open("sample.pdf").use { input ->
                targetFile.outputStream().use { output -> input.copyTo(output) }
            }
        }
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            targetFile
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        Toast.makeText(
            context,
            "Could not open PDF. Place 'sample.pdf' in app/src/main/assets/.",
            Toast.LENGTH_LONG
        ).show()
    }
}
