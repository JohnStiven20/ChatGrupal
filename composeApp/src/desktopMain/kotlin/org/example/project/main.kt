package org.example.project

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

    val socketRepository = SocketRepository(dispatchers =  CoroutineDispatchers())
    val viewModel = ViewModel(socketRepository)
    var isAppRunning by remember { mutableStateOf(true) }

    val closeConnection = {
        if (viewModel.estadoConexion.value) {
            viewModel.sendMessage("EXI")
            viewModel.closeConnection()
            viewModel.resetStates()
        }
        isAppRunning = false
    }

    if (isAppRunning) {
        Window(
            onCloseRequest = closeConnection,
            title = "Chat Grupal",
        ) {
            App(viewModel = viewModel)
        }
    }
}
