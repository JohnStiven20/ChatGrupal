package org.example.project

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.project.repository.CoroutineDispatchers
import org.example.project.repository.SocketRepository
import org.example.project.ui.nickname.ViewModel

fun main() = application {

    val socketRepository = SocketRepository(dispatchers = CoroutineDispatchers())
    val viewModel = ViewModel(socketRepository)
    var isAppRunning by remember { mutableStateOf(true) }
    var nombre by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.nickname) {
        viewModel.nickname.collect { nuevoNombre ->
            nombre = nuevoNombre
        }
    }

    val closeConnection = {
        println("El usuario saldrá del chat: $nombre")
        if (viewModel.estadoConexion.value) {
            viewModel.sendMessage("EXI")
            viewModel.sendMessage("MSG $nombre ha salido del chat")
        }

        isAppRunning = false
        System.exit(0)
    }

    // Mostrar la ventana solo si la aplicación está activa
    if (isAppRunning) {
        Window(
            onCloseRequest = closeConnection,
            title = "Chat Grupal",
        ) {
            App(viewModel = viewModel)
        }
    }
}