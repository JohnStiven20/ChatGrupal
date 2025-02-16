package org.example.project.ui.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.img
import org.example.project.navigation.Screen
import org.example.project.ui.nickname.Mensaje
import org.example.project.ui.nickname.ViewModel
import org.jetbrains.compose.resources.painterResource


@Composable
fun ChatPrivadoScreen(
    viewModel: ViewModel,
    pantallaActual: MutableState<Screen>
) {
    val nombreDelOtro by viewModel.nicknamePrivado.collectAsState()
    val  nickname by viewModel.nickname.collectAsState()
    val nombreMio by viewModel.nickname.collectAsState()
    val usuarios by viewModel.listaUsuarios.collectAsState()
    val mapaUsuarios by viewModel.mapaUsuarios.collectAsState()

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        content = {
            ChatIzquierda(usuarios, nombreMio, pantallaActual,
                onClick = {
                    viewModel.setCambioUsuario(nombre = it)
                }
            )
            ChatScreenPrivado(
                onPromtChange = {
                    viewModel.sendMessagePrivado(it)
                    val mensaje = it.substring(it.lastIndexOf(",") + 1, it.length)
                    viewModel.addMap(nombreDelOtro, Mensaje(usario = nombreMio, mensaje = mensaje))
                },
                closeConnection = {
                    if (viewModel.estadoConexion.value) {
                        viewModel.sendMessage("EXI")
                    } else {
                        println("Intento de enviar 'EXI' en una conexión cerrada.")
                    }
                    pantallaActual.value = Screen.NickName
                },
                usuarioDelOtro = nombreDelOtro,
                usuarioMio = nombreMio,
                mapaUsuarios = mapaUsuarios,
            )
        }
    )
}


@Composable
fun ChatScreenPrivado(
    onPromtChange: (String) -> Unit,
    closeConnection: () -> Unit,
    usuarioDelOtro: String,
    usuarioMio: String,
    mapaUsuarios: MutableMap<String, MutableList<Mensaje>>,


    ) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBarPrivado(adress = usuarioDelOtro, closeConnection = closeConnection)
        },
        content = { padding ->
            ContenidoMensajePrivado(
                padding = padding,
                mapaUsuarios = mapaUsuarios,
                nombreMio = usuarioMio,
                nombreDelOtro = usuarioDelOtro

            )
        },
        bottomBar = {
            AppBottomBarPrivado(
                onPromtChange = onPromtChange,
                usuarioDelOtro = usuarioDelOtro,
            )
        }
    )
}


@Composable
fun AppBarPrivado(adress: String, closeConnection: () -> Unit) {
    TopAppBar(
        backgroundColor = Color.DarkGray,
        title = {
            Text(text = adress, color = Color.White)
        },
        actions = {
            IconButton(onClick = closeConnection) {
                Image(
                    modifier = Modifier.size(36.dp).clip(CircleShape),
                    painter = painterResource(resource = Res.drawable.img) ,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        }
    )
}

@Composable
fun ContenidoMensajePrivado(
    padding: PaddingValues,
    nombreDelOtro: String,
    nombreMio: String,
    mapaUsuarios: MutableMap<String, MutableList<Mensaje>>
) {

    val listState = rememberLazyListState()
    val messages = mapaUsuarios[nombreDelOtro] ?: listOf()

    LaunchedEffect(messages) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(Color.LightGray),
        state = listState
    ) {
        items(messages) { message ->

            val identificador = message.usario
            val mensaje = message.mensaje

            if (identificador == nombreMio) {
                MensajePrivado(
                    text = mensaje,
                    nicknamee = identificador,
                    alignment = Alignment.TopEnd,
                    alignmentText = Alignment.Start,
                    color = Color.Blue
                )
            } else {
                MensajePrivado(
                    text = mensaje,
                    nicknamee = identificador,
                    alignment = Alignment.TopStart,
                    alignmentText = Alignment.Start,
                    color = Color.Magenta
                )
            }

        }
    }
}


@Composable
fun MensajePrivado(
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
fun AppBottomBarPrivado(onPromtChange: (String) -> Unit, usuarioDelOtro: String) {

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
                            val mensaje = "PRV $usuarioDelOtro,$texto"
                            onPromtChange(mensaje)
                            texto = ""
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
                    val mensaje = "PRV $usuarioDelOtro,$texto"
                    onPromtChange(mensaje)
                    texto = ""
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


fun Modifier.addLine1(
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