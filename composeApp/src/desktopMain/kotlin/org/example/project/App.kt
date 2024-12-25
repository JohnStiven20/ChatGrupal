package org.example.project

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.remember
import org.example.project.Screen.*
import org.example.project.repository.SocketRepository
import org.example.project.repository.CoroutineDispatchers
import org.example.project.ui.chat.ChatApp
import org.example.project.ui.chat.ChatPersonal
import org.example.project.ui.nickname.NickNameScreen
import org.example.project.ui.nickname.ViewModel
import java.awt.Window as AwtWindow


@Composable
@Preview()
fun App() {

    val socketRepository = SocketRepository()

    MaterialTheme(
        colors = MaterialTheme.colors,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes
    ) {
        val nickNameViewModel = ViewModel(socketRepository, CoroutineDispatchers())
        AppNavigation(nickNameViewModel)
    }
}

enum class Screen {
    NickName,
    Chat
}

@Composable
fun AppNavigation(nickNameViewModel: ViewModel) {

    val currentScreen = remember { mutableStateOf<Screen>(NickName) }

    when (currentScreen.value) {

        NickName -> NickNameScreen(
            onNavigateToSettings = { currentScreen.value = Chat },
            nickNameViewModel = nickNameViewModel
        )
        Chat -> ChatPersonal(
            viewModel = nickNameViewModel,
            onNavigateBack = {currentScreen.value = NickName}
        )
    }
}


