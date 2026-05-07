package com.example.a5_3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a5_3.ui.theme._5_3Theme

// === Composition Locals fuer Style + Alpha ===
val LocalTextAlpha = compositionLocalOf { 1.0f }
val LocalAppTextStyle = staticCompositionLocalOf { TextStyle.Default }

// === User-Datenmodell ===
data class User(val name: String, val address: String, val phone: String)

val sampleUser = User(
    name    = "John Doe",
    address = "123 Main Street, Springfield",
    phone   = "555-123-4567"
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _5_3Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CompareScreen()
                }
            }
        }
    }
}

@Composable
fun CompareScreen() {
    val highlightStyle = TextStyle(
        color = Color.Red,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )
    val highlightAlpha = 0.3f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Variant 1: CompositionLocals", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        CompositionLocalProvider(
            LocalTextAlpha provides highlightAlpha,
            LocalAppTextStyle provides highlightStyle
        ) {
            UserProfileImplicit(user = sampleUser)
        }

        Spacer(Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(Modifier.height(24.dp))

        Text("Variant 2: Explicit parameters", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        UserProfileExplicit(
            user = sampleUser,
            alpha = highlightAlpha,
            style = highlightStyle
        )
    }
}

// =====================================================================
// Variant 1: implicit via CompositionLocal
// =====================================================================

@Composable
fun UserProfileImplicit(user: User) {
    Column {
        UserDetailsImplicit(user)
        Spacer(Modifier.height(8.dp))
        GreetingImplicit(user.name)
    }
}

@Composable
fun UserDetailsImplicit(user: User) {
    Column {
        NameImplicit(user.name)
        AddressImplicit(user.address)
        PhoneImplicit(user.phone)
    }
}

@Composable
fun NameImplicit(name: String) {
    LabelImplicit(label = "Name:", value = name)
}

@Composable
fun AddressImplicit(address: String) {
    LabelImplicit(label = "Address:", value = address)
}

@Composable
fun PhoneImplicit(phone: String) {
    LabelImplicit(label = "Phone Number:", value = phone)
}

@Composable
fun GreetingImplicit(name: String) {
    val style = LocalAppTextStyle.current
    val alpha = LocalTextAlpha.current
    Text(
        text = "Welcome back, $name!",
        style = style.copy(color = style.color.copy(alpha = alpha))
    )
}

@Composable
private fun LabelImplicit(label: String, value: String) {
    val style = LocalAppTextStyle.current
    val alpha = LocalTextAlpha.current
    val baseColor = style.color
    val faded = baseColor.copy(alpha = alpha)
    Text(text = "$label $value", style = style.copy(color = faded))
}

// =====================================================================
// Variant 2: explicit per Parameter ("Prop Drilling")
// =====================================================================

@Composable
fun UserProfileExplicit(user: User, alpha: Float, style: TextStyle) {
    Column {
        UserDetailsExplicit(user, alpha, style)
        Spacer(Modifier.height(8.dp))
        GreetingExplicit(user.name, alpha, style)
    }
}

@Composable
fun UserDetailsExplicit(user: User, alpha: Float, style: TextStyle) {
    Column {
        NameExplicit(user.name, alpha, style)
        AddressExplicit(user.address, alpha, style)
        PhoneExplicit(user.phone, alpha, style)
    }
}

@Composable
fun NameExplicit(name: String, alpha: Float, style: TextStyle) {
    LabelExplicit(label = "Name:", value = name, alpha = alpha, style = style)
}

@Composable
fun AddressExplicit(address: String, alpha: Float, style: TextStyle) {
    LabelExplicit(label = "Address:", value = address, alpha = alpha, style = style)
}

@Composable
fun PhoneExplicit(phone: String, alpha: Float, style: TextStyle) {
    LabelExplicit(label = "Phone Number:", value = phone, alpha = alpha, style = style)
}

@Composable
fun GreetingExplicit(name: String, alpha: Float, style: TextStyle) {
    Text(
        text = "Welcome back, $name!",
        style = style.copy(color = style.color.copy(alpha = alpha))
    )
}

@Composable
private fun LabelExplicit(label: String, value: String, alpha: Float, style: TextStyle) {
    val faded = style.color.copy(alpha = alpha)
    Text(text = "$label $value", style = style.copy(color = faded))
}
