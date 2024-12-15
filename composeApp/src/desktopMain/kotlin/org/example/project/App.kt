package org.example.project

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.remember
import org.example.project.Screen.*
import org.example.project.ui.chat.ChatScreen
import org.example.project.ui.nickname.NickNameScreen
import org.example.project.ui.nickname.ViewModel

@Composable
@Preview()
fun App() {
    MaterialTheme {

        val nickNameViewModel = ViewModel()
        AppNavigation(nickNameViewModel = nickNameViewModel)
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
        Chat -> ChatScreen(
            onNavigateBack = { currentScreen.value = NickName },
            nickNameViewModel = nickNameViewModel
        )
    }
}


@Composable
fun Hola() {



}