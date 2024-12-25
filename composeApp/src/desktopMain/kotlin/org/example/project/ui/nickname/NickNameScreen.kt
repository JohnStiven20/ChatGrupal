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


@Composable
fun NickNameScreen(onNavigateToSettings: () -> Unit, nickNameViewModel: ViewModel) {

    val nickName by nickNameViewModel.nickname.collectAsState()
    val entrada by nickNameViewModel.entrada.collectAsState()
    val isConnected by nickNameViewModel.isConnected.collectAsState()

    var showError  by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        nickNameViewModel.connection()
    }

    Column(
        modifier = Modifier.fillMaxSize().background(color = Color(0xFF2E3035)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Introduce un Nickname Stiven")

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = nickName,
            onValueChange = { nickNameViewModel.onChange(nickName = it) },
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

        if (showError) {
            Text("ALGO VA MAL", color = Color.Red)
        }

        Button(
            onClick = {
                val message = "CON,$nickName"
                // Enviamos el mensaje y esperamos la respuesta "OK" o "NOK"
                nickNameViewModel.sendMessage(
                    message = message,
                    onProgress = { isSuccess ->
                        if (isSuccess) {
                            // 'entrada' contiene la respuesta del servidor
                            val response = entrada.trim().uppercase()
                            if (response == "OK") {
                                onNavigateToSettings()
                            } else if (response == "") {
                                showError = true
                                nickNameViewModel.onChange("MARIA JOSE")
                            } else {
                                showError = true
                                nickNameViewModel.onChange("JOSE MARIA")
                            }
                        } else {
                            // onProgress(false) -> indica que hubo un fallo al enviar/recibir
                            showError = true
                        }
                    }
                )
            },
            shape = CircleShape,
            enabled = isConnected // Habilita el botón solo si está conectado
        ) {
            Text("Conectarse")
        }

    }
}

