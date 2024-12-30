package org.example.project.ui.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.example.project.ui.nickname.ViewModel


@Composable
fun ScreenHola(viewModel: ViewModel, onNavigateBack: () -> Unit) {

    var texto = remember { mutableStateOf("") }
    val entrada by viewModel.entradaChat.collectAsState()

    Scaffold(
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Text(text = " $entrada Hola", color = Color.Black)
                },
            )
        },
        bottomBar = {
            TextField(
                modifier = Modifier.fillMaxWidth().padding(8.dp).clickable(
                    onClick = {
                        val enviar = "MSG,${texto.value}"
                        viewModel.sendMessage(enviar)
                        texto.value = ""
                    }
                ),
                onValueChange = {
                    texto.value = it
                },
                value = texto.value
            )
        }
    )

}