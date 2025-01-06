package org.example.project

import AppNavigation
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import org.example.project.repository.CoroutineDispatchers
import org.example.project.repository.SocketRepository
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
        AppNavigation(nickNameViewModel)
    }
}





