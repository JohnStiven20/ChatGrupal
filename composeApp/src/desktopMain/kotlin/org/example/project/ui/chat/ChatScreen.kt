package org.example.project.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun ChatApp() {
    val messages = remember {
        mutableStateListOf(
            "¡Buenos días! Nos han informado...",
            "Esto es una estafa?",
            "Sí amigo, como LUNA",
            "El qué?",
            "Una publicación de OVR falsa",
            "Para estafar",
            "Tengan cuidado",
            "Good night"
        )
    }
    val users = remember { mutableStateListOf("Dan", "Jota Be", "Xavi Pars", "Lari") }
    val currentMessage = remember { mutableStateOf("") }
    val sidebarWidth = remember { mutableStateOf(200.dp) } // Ancho inicial del Sidebar

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("OVER - Español 🇪🇸", color = Color.White) },
                backgroundColor = Color(0xFF2E3035),
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.White)
                    }
                }
            )
        },
        content = { padding ->
            Row(Modifier.fillMaxSize().padding(padding)) {
                // Sidebar con funcionalidad de redimensionamiento
                SideBar(users, sidebarWidth)
                ChatContent(messages)
            }
        },
        bottomBar = {
            BottomBar(currentMessage, onSend = { message ->
                if (message.isNotBlank()) {
                    messages.add(message)
                    currentMessage.value = ""
                }
            })
        }
    )
}

@Composable
fun SideBar(users: List<String>, sidebarWidth: MutableState<Dp>) {
    Box(
        modifier = Modifier
            .width(sidebarWidth.value)
            .fillMaxHeight()
            .background(Color(0xFF3A3C42))
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    sidebarWidth.value = (sidebarWidth.value + dragAmount.x.dp).coerceIn(
                        100.dp,
                        400.dp
                    )
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(8.dp)
        ) {
            users.forEach { user ->
                Text(
                    text = user,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF2E3035))
                        .padding(8.dp),
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Composable
fun ChatContent(messages: List<String>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E))
            .padding(8.dp)
    ) {
        items(messages) { message ->
            ChatMessage("User", message)
        }
    }
}

@Composable
fun ChatMessage(user: String, message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Text(
            text = user,
            style = MaterialTheme.typography.subtitle2,
            color = Color(0xFF64B5F6)
        )
        Text(
            text = message,
            style = MaterialTheme.typography.body2,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF2E3035))
                .padding(8.dp)
        )
    }
}

@Composable
fun BottomBar(currentMessage: MutableState<String>, onSend: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2E3035))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = currentMessage.value,
            onValueChange = { currentMessage.value = it },
            placeholder = { Text("Type a message...", color = Color.Gray) },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
                .background(Color(0xFF3A3C42), shape = RoundedCornerShape(10.dp)),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                textColor = Color.White
            )
        )
        IconButton(onClick = { onSend(currentMessage.value) }) {
            Icon(Icons.Default.Send, contentDescription = null, tint = Color(0xFF64B5F6))
        }
    }
}

@Composable
fun ResizableBoxWithMouse() {
    val boxWidth = remember { mutableStateOf(200.dp) }

    Box(
        modifier = Modifier
            .width(boxWidth.value)
            .fillMaxHeight()
            .background(Color.LightGray)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume() // Consume el evento
                        boxWidth.value = (boxWidth.value + dragAmount.x.dp)
                            .coerceIn(100.dp, 400.dp) // Limita el ancho
                    }
                )
            }
    ) {
        Text("Arrastra con el ratón", modifier = Modifier.align(Alignment.Center))
    }
}

