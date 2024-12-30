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
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import javax.swing.JOptionPane


@Composable
fun NickNameScreen(onNavigateToSettings: () -> Unit, nickNameViewModel: ViewModel) {

    val nickName by nickNameViewModel.nickname.collectAsState()
    val entrada by nickNameViewModel.entrada.collectAsState()
    var showError by remember { mutableStateOf(false) }

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
                ).clip(RoundedCornerShape(percent = 25)).background(color = Color.White)
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            if (event.type == PointerEventType.Press && event.buttons.isPrimaryPressed) {
                                showError = false
                            }
                        }
                    }
                },
            maxLines = 1,
            singleLine = true,
            label = { Text("Nickname") },
            shape = RoundedCornerShape(percent = 25)
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (showError) {
            Text("Hola buenas tardes a todos", color = Color.Red)
        }

        Text(entrada)

        Button(
            onClick = {
                val message = "CON,$nickName"
                nickNameViewModel.sendMessage(message = message)
            },
            shape = CircleShape,
            enabled = true
        ) {
            Text("Conectarse")

        }

        if (entrada != "") {
            val comando = entrada.substring(0, entrada.indexOf(","))
            if (comando == "NOK") {
                showError = true
            } else if (comando == "OK") {
                onNavigateToSettings()
                nickNameViewModel.sendMessage("LUS,")

            }
        }
    }
}

/**
 * IMPORTANTE PARA MIRAR
 *
 * Muestra un mensaje de error en forma de Toast-like.
 *
 */

fun showSwingDialog(message: String) {
    JOptionPane.showMessageDialog(null, message)
}


