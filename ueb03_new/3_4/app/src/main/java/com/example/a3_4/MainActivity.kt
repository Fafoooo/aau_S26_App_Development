package com.example.a3_4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a3_4.ui.theme._3_4Theme

enum class MessageType { Inbound, Outbound }

data class Message(
    val sender: String,
    val text: String,
    val timestamp: String,
    val type: MessageType
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _3_4Theme {
                ChatScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    val messages = remember { mutableStateListOf(*sampleMessages().toTypedArray()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compose Chat") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val nr = messages.size + 1
                messages.add(
                    Message(
                        sender = if (nr % 2 == 0) "Me" else "Anna",
                        text = "New message #$nr",
                        timestamp = "12:%02d".format(nr % 60),
                        type = if (nr % 2 == 0) MessageType.Outbound else MessageType.Inbound
                    )
                )
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add message")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(messages) { message ->
                ChatBubble(message)
            }
        }
    }
}

@Composable
fun ChatBubble(message: Message) {
    val isOutbound = message.type == MessageType.Outbound
    val bubbleColor = if (isOutbound) MaterialTheme.colorScheme.primary else Color(0xFFE0E0E0)
    val textColor = if (isOutbound) Color.White else Color.Black
    val alignment = if (isOutbound) Alignment.CenterEnd else Alignment.CenterStart
    val shape = if (isOutbound) {
        RoundedCornerShape(16.dp, 16.dp, 4.dp, 16.dp)
    } else {
        RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp)
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(shape)
                .background(bubbleColor)
                .padding(12.dp)
        ) {
            if (!isOutbound) {
                Text(
                    text = message.sender,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = message.text,
                color = textColor
            )
            Text(
                text = message.timestamp,
                fontSize = 10.sp,
                color = if (isOutbound) Color.White.copy(alpha = 0.7f) else Color.Gray,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

fun sampleMessages(): List<Message> {
    return listOf(
        Message("Anna", "Hey! How are you?", "10:00", MessageType.Inbound),
        Message("Me", "Hi Anna! I'm good, thanks!", "10:01", MessageType.Outbound),
        Message("Anna", "Did you finish the assignment?", "10:02", MessageType.Inbound),
        Message("Me", "Almost, still working on task 4", "10:03", MessageType.Outbound),
        Message("Anna", "The LazyColumn part is pretty cool actually", "10:04", MessageType.Inbound),
        Message("Me", "Yeah, way less boilerplate than RecyclerView", "10:05", MessageType.Outbound),
        Message("Anna", "Exactly! No adapter, no ViewHolder", "10:06", MessageType.Inbound),
        Message("Me", "Just items {} and you're done", "10:07", MessageType.Outbound),
        Message("Anna", "Have you tried the FAB yet?", "10:08", MessageType.Inbound),
        Message("Me", "Working on that right now", "10:09", MessageType.Outbound),
        Message("Anna", "Let me know if you need help", "10:10", MessageType.Inbound),
        Message("Me", "Will do, thanks!", "10:11", MessageType.Outbound),
        Message("Anna", "By the way, did you see the Scaffold docs?", "10:15", MessageType.Inbound),
        Message("Me", "The one about TopAppBar slots?", "10:16", MessageType.Outbound),
        Message("Anna", "Yeah, it makes the layout structure so clean", "10:17", MessageType.Inbound),
        Message("Me", "True, everything is just composable functions", "10:18", MessageType.Outbound),
        Message("Anna", "No XML needed at all", "10:19", MessageType.Inbound),
        Message("Me", "That's what I like about Compose", "10:20", MessageType.Outbound),
        Message("Anna", "Same here. Are you coming to the lab tomorrow?", "10:25", MessageType.Inbound),
        Message("Me", "Yes, at 10 as usual", "10:26", MessageType.Outbound),
        Message("Anna", "Great, see you there!", "10:27", MessageType.Inbound),
        Message("Me", "See you!", "10:28", MessageType.Outbound),
        Message("Anna", "Oh wait, one more thing", "10:30", MessageType.Inbound),
        Message("Anna", "Did you figure out the state hoisting part?", "10:30", MessageType.Inbound),
        Message("Me", "Yeah, it's about moving state up to the caller", "10:31", MessageType.Outbound),
        Message("Me", "So the composable itself stays stateless", "10:31", MessageType.Outbound),
        Message("Anna", "Makes sense, thanks for explaining!", "10:32", MessageType.Inbound),
        Message("Me", "No problem!", "10:33", MessageType.Outbound)
    )
}

@Preview(showBackground = true)
@Composable
fun ChatPreview() {
    _3_4Theme {
        ChatScreen()
    }
}
