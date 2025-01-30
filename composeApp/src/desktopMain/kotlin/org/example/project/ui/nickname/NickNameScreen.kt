package org.example.project.ui.nickname

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.example.project.navigation.Screen
import javax.swing.JOptionPane


@Composable
fun NickNameScreen(viewModel: ViewModel, pantallaActual: MutableState<Screen>) {

    val nickName by viewModel.nickname.collectAsState()
    val mensajeServidor by viewModel.entrada.collectAsState()
    var mostrarError by remember { mutableStateOf(false) }
    val estadoConexion by viewModel.estadoConexion.collectAsState()
    var nombreLocal by remember { mutableStateOf("") }

    LaunchedEffect(Unit, estadoConexion) {
        viewModel.recibirMensajeServidor()
    }

    Column(
        modifier = Modifier.fillMaxSize().background(color = Color(0xFF2E3035)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Introduce un Nickname")

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = nombreLocal,
            onValueChange = { nombreLocal = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp).border(
                    1.5.dp,
                    MaterialTheme.colorScheme.onBackground,
                    RoundedCornerShape(percent = 25)
                ).clip(RoundedCornerShape(percent = 25)).background(color = Color.White),
            maxLines = 1,
            singleLine = true,
            label = { Text("Nickname") },
            shape = RoundedCornerShape(percent = 25)
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (mostrarError) {
            mostrarMensajeError("El nombre ya existe")
            mostrarError = false
        }

        Button(
            onClick = {
                viewModel.onChange(nombreLocal.trim())
                viewModel.connection { conectado ->
                    println("Conectado: $conectado")
                    if (conectado) {
                        val message = "CON $nickName"
                        println("Mensaje enviado: $message")
                        viewModel.encender()
                        viewModel.sendMessage(message = message)
                    }
                }

            },
            shape = CircleShape,
        ) {
            Text("Conectarse")
        }

        if (mensajeServidor != "") {

            val comando = mensajeServidor.split(" ")[0]

            if (comando == "NOK") {
                mostrarError = true
                viewModel.onChangeEntrada("")
                viewModel.onChange("")
            } else if (comando == "OK") {
                pantallaActual.value = Screen.ChatGeneral
                viewModel.sendMessage("LUS")
            }
        }
    }
}


fun mostrarMensajeError(mensaje: String) {
    JOptionPane.showMessageDialog(null, mensaje)
}


