package org.example.project.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.Screen
import org.example.project.ui.nickname.ViewModel


@Composable
fun ChatPersonal(
    viewModel: ViewModel,
    onNavigateBack: () -> Unit,
    currentScreen: MutableState<Screen>
) {

    val nombreUsuario by viewModel.nickname.collectAsState()
    val entradaChat by viewModel.entradaChat.collectAsState()
    val usuarios by viewModel.nombreUsuario.collectAsState()

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        content = {
            ChatIzquierda(usuarios, nombreUsuario, currentScreen,
                onClick = {
                    viewModel.setOnchangeUser(nombre = it)
                }
            )
            ChatScreen(
                onPromtChange = { prompt ->
                    viewModel.sendMessage(prompt)
                },
                closeConnection = {
                    onNavigateBack()
                },
                adress = nombreUsuario,
                entrada = entradaChat
            )
        }
    )
}

/**
 * Representa una lista de chats en una columna  con estilo básico.
 *
 * @param users Lista de nombres de usuarios que se mostrarán como elementos de la lista.
 *
 * Este composable crea una columna  (`LazyColumn`) que ocupa toda la altura disponible y
 * tiene un ancho fijo de 200dp. El fondo es de color gris claro y contiene un borde negro.
 *
 * Contenido:
 * - Un encabezado con el texto "Chats".
 * - Una lista dinámica de tarjetas de usuario, generada a partir de los elementos en la lista `users`.
 *
 * La lista utiliza `verticalArrangement` para espaciar los elementos uniformemente.
 */

@Composable
fun ChatIzquierda(
    clientesNombre: String,
    nombreUsuario: String,
    currentScreen: MutableState<Screen>,
    onClick: (String) -> Unit
) {

    if (clientesNombre.isNotBlank()) {

        val listaNombres = clientesNombre.substring(4).split(",").map { it.trim() }

        LazyColumn(
            modifier = Modifier.fillMaxHeight().width(200.dp).background(Color.LightGray)
                .addLine(color = Color.Black, position = "right", width = 1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            content = {
                item(
                    content = {
                        Text(
                            "Chats",
                            color = Color.Black,
                            modifier = Modifier.padding(10.dp),
                            fontSize = 25.sp
                        )
                    }
                )

                item(content = {
                    TarjetaUsuario(
                        nombre = "2 DAM",
                        currentScreen = currentScreen,
                        onClick = onClick,
                        screen = Screen.ChatGeneral
                    )
                })


                items(listaNombres) { nombre ->
                    if (nombre != nombreUsuario) {
                        TarjetaUsuario(nombre = nombre, currentScreen, onClick)
                    }
                }
            }
        )
    }
}

@Composable
fun TarjetaUsuario(
    nombre: String,
    currentScreen: MutableState<Screen> = mutableStateOf(Screen.ChatPrivado),
    onClick: (String) -> Unit,
    screen: Screen = Screen.ChatPrivado
) {
    println("Nombre recibido en TarjetaUsuario: $nombre")

    Card(
        backgroundColor = Color.Gray,
        modifier = Modifier.fillMaxWidth().padding(5.dp).clip(RoundedCornerShape(10.dp))
            .clickable(onClick = {
                currentScreen.value = screen
                onClick(nombre)
            }),
        shape = RoundedCornerShape(10.dp),
        content = {
            Box(
                modifier = Modifier.padding(10.dp),
                contentAlignment = Alignment.Center,
                content = {
                    Text(nombre)
                }
            )
        }
    )

}

@Composable
fun ChatScreen(
    onPromtChange: (String) -> Unit,
    closeConnection: () -> Unit,
    adress: String,
    entrada: String,

    ) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(adress = adress)
        },
        content = { padding ->
            ContenidoMensaje(
                padding = padding,
                onPromtChange = onPromtChange,
                adress = adress,
                entrada = entrada
            )
        },
        bottomBar = {
            AppBottomBar(
                onPromtChange = onPromtChange,
                adress = adress,

                )
        },
        floatingActionButton = {
            BotonFlotante(
                closeConnection = closeConnection
            )
        }
    )
}


@Composable
fun AppBar(adress: String) {
    TopAppBar(
        backgroundColor = Color.DarkGray,
        title = {
            Text(text = adress, color = Color.White)
        },
        actions = {
            IconButton(
                onClick = {

                }
            ) {
                Text("Hola")
            }
        }
    )
}

@Composable
fun ContenidoMensaje(
    padding: PaddingValues,
    onPromtChange: (String) -> Unit,
    adress: String,
    entrada: String
) {

    val messages = rememberSaveable { mutableStateListOf<String>() }
    val listState = rememberLazyListState()

    LaunchedEffect(entrada) {
        if (entrada.isNotBlank()) {
            val comando = entrada.substring(0, entrada.indexOf(","))

            if (comando == "CHT") {
                messages.add(entrada)
            }
        }
    }

    LaunchedEffect(entrada) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding).background(Color.LightGray),
        content = {

            items(messages) { message ->

                val comando = message.substring(0, message.indexOf(",")).uppercase()

                if (comando == "CHT") {

                    val indetificador =
                        message.substring(message.indexOf(",") + 1, message.lastIndexOf(",")).trim()
                    val mensaje = message.substring(message.lastIndexOf(",") + 1, message.length)

                    if (indetificador == adress) {
                        Mensaje(
                            text = mensaje,
                            nicknamee = indetificador,
                            alignment = Alignment.TopEnd,
                            alignmentText = Alignment.Start,
                            color = Color.Blue
                        )
                    } else {
                        Mensaje(
                            text = mensaje,
                            nicknamee = indetificador,
                            alignment = Alignment.TopStart,
                            alignmentText = Alignment.Start,
                            color = Color.Magenta
                        )
                    }
                }
            }
        },
        state = listState
    )
}

@Composable
fun Mensaje(
    text: String,
    alignment: Alignment,
    alignmentText: Alignment.Horizontal = Alignment.End,
    nicknamee: String = "",
    color: Color = Color.Green

) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        content = {
            Card(
                backgroundColor = color,
                modifier = Modifier.padding(10.dp).align(alignment),
                shape = RoundedCornerShape(10.dp),
                content = {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        content = {
                            Text(
                                modifier = Modifier.align(alignment = alignmentText),
                                text = nicknamee,
                                color = Color.Green,
                                textAlign = TextAlign.End,
                                fontSize = 11.sp
                            )
                            Text(text = text)
                        }
                    )
                }
            )
        }
    )
}

@Composable
fun AppBottomBar(onPromtChange: (String) -> Unit, adress: String) {

    var texto by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(end = 10.dp)
                    .onKeyEvent { keyEvent ->

                        if (keyEvent.key == Key.Enter && keyEvent.type == KeyEventType.KeyUp) {
                            if (texto.isNotBlank()) {
                                onPromtChange("MSG,$texto")
                                texto = ""

                            }
                            true
                        } else {
                            false
                        }
                    },
                value = texto,
                onValueChange = { texto = it },
                placeholder = { Text("Escribe un mensaje...") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedLabelColor = Color.Transparent,
                    focusedLabelColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )

            IconButton(
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color.DarkGray),
                onClick = {
                    if (texto.isNotBlank()) {
                        onPromtChange("MSG,$texto")
                        texto = "" // Limpia el campo después de enviar
                    }
                },
                content = {
                    Icon(
                        Icons.AutoMirrored.Default.Send,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            )
        }
    )
}

@Composable
fun BotonFlotante(closeConnection: () -> Unit) {
    IconButton(
        modifier = Modifier.clip(shape = RoundedCornerShape(25.dp))
            .background(Color.DarkGray), onClick = closeConnection, content = {
            Icon(
                Icons.AutoMirrored.Default.Send,
                contentDescription = null,
                tint = Color.White
            )
        }
    )
}


fun Modifier.addLine(
    color: Color = Color.Black,
    width: Dp = 2.dp,
    position: String
): Modifier {
    return this.then(
        Modifier.drawBehind {
            val strokeWidth = width.toPx()
            when (position) {
                "left" -> drawLine(
                    color = color,
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = strokeWidth
                )

                "right" -> drawLine(
                    color = color,
                    start = Offset(size.width, 0f),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )

                "top" -> drawLine(
                    color = color,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = strokeWidth
                )

                "bottom" -> drawLine(
                    color = color,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
            }
        }
    )
}