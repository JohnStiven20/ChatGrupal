package org.example.project.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import org.example.project.ui.nickname.ViewModel
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

@Composable
fun ChatScreen(nickNameViewModel: ViewModel, onNavigateBack: () -> Unit) {

    val nickName by nickNameViewModel.nickname.collectAsState()
    val colorGreyLight = Color(0xFF3A3C42)
    val colorGreyDark = Color(0xFF2E3035)
    val messageList = remember { mutableStateListOf<String>() }
    val socket = Socket("localhost", 4444)
    val entrada = DataInputStream(socket.getInputStream())
    val salida = DataOutputStream(socket.getOutputStream())


    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = colorGreyDark,
                title = { Text("Nickname: $nickName", fontStyle = FontStyle.Italic) },
            )
        },
        content = { paddingValues ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorGreyLight)
                    .padding(paddingValues),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Body(messageList)
            }
        },
        bottomBar = {
            TextoAbajo(
                color = colorGreyLight,
                onNavigateBack = onNavigateBack, onSendMessage = {
                    messageList.add(it)
                    nickNameViewModel.onPromt("")
                }, salida = salida
            )
        }
    )
}

@Composable
fun TextoAbajo(
    color: Color,
    onNavigateBack: () -> Unit,
    onSendMessage: (String) -> Unit,
    salida: DataOutputStream
) {
    var text by remember { mutableStateOf("") } // Variable local para el

    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        backgroundColor = Color(0xFF2E3035),
        content = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp, end = 8.dp)
                        .border(
                            width = 0.2.dp,
                            color = Color(0xFF2E3035),
                            shape = RoundedCornerShape(percent = 25)
                        )
                        .background(color = color, shape = RoundedCornerShape(percent = 25))
                        .onKeyEvent {
                            if (it.key == Key.Enter) {
                                salida.writeUTF(text)
                                onSendMessage(text)
                                true
                            } else {
                                false
                            }
                        },
                    maxLines = 1,
                    singleLine = true,
                    shape = RoundedCornerShape(percent = 25),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color(0xFFEEEEEE),
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                    ),
                    textStyle = TextStyle(Color(0xFFEEEEEE))
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(percent = 30))
                        .background(color = Color(0xFFE0E0E0))
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
        }
    )
}

@Composable
fun Terminal(usuarios: List<String>, messageList: List<String>) {

    Box(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .padding(16.dp).clip(RoundedCornerShape(15.dp)).background(Color(0xFF2E3035)),
    ) {
        LazyColumn(modifier = Modifier.fillMaxHeight().padding(16.dp)) {
            items(messageList) { message ->

                val myText = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Blue)) {
                        append(usuarios[0])
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            fontStyle = FontStyle.Italic
                        )
                    ) {
                        append(": $message")
                    }
                }

                Text(
                    text = myText,
                    modifier = Modifier.fillMaxWidth()
                        .wrapContentSize(Alignment.TopStart) // Alinear a la derecha
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}

@Composable
fun Body(messageList: List<String>) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Terminal(listOf("Stiven"), messageList)
        TablaUsuarios(listOf())
    }
}

@Composable
fun TablaUsuarios(users: List<String>) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.CenterEnd,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            items(12) { user ->
                TarjetaUsuario("user")
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun TarjetaUsuario(user: String) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp
    ) {
        TextButton(onClick = {}) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                    Text(
                        text = "Marcos Perez",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface
                    )

            }
        }
    }
}
