package org.example.project

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.example.project.Screen.ChatGeneral
import org.example.project.Screen.ChatPrivado
import org.example.project.Screen.NickName
import org.example.project.repository.CoroutineDispatchers
import org.example.project.repository.SocketRepository
import org.example.project.ui.chat.ChatPersonal
import org.example.project.ui.chat.ChatPersonal1
import org.example.project.ui.nickname.NickNameScreen
import org.example.project.ui.nickname.ViewModel


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

        LaunchedEffect(Unit) {
            nickNameViewModel.recibirMensaje {}
        }

        AppNavigation(nickNameViewModel)
    }
}

enum class Screen {
    NickName,
    ChatGeneral,
    ChatPrivado
}

@Composable
fun AppNavigation(nickNameViewModel: ViewModel) {

    val currentScreen = remember { mutableStateOf<Screen>(NickName) }

    when (currentScreen.value) {

        NickName -> NickNameScreen(
            onNavigateToSettings = { currentScreen.value = ChatGeneral },
            nickNameViewModel = nickNameViewModel
        )

        ChatGeneral -> ChatPersonal(
            viewModel = nickNameViewModel,
            onNavigateBack = { currentScreen.value = ChatPrivado },
            currentScreen = currentScreen
        )

        ChatPrivado -> {
            ChatPersonal1(
                viewModel = nickNameViewModel,
                onNavigateBack = { currentScreen.value = ChatGeneral },
                currentScreen = currentScreen
            )
        }

    }
}



