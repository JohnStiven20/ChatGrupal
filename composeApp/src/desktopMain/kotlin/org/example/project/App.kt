package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.compose_multiplatform
import org.example.project.ui.HomeScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.example.project.ui.LoginScreen
import org.example.project.ui.SettingsScreen


@Composable
@Preview
fun App() {

    MaterialTheme {
        AppNavigation()
    }
}

enum class Screen {
    Home,
    Details,
    Settings
}
@Preview
@Composable
fun AppNavigation() {

    val currentScreen = remember { mutableStateOf<Screen>(Screen.Home) }

    when (currentScreen.value) {

        Screen.Home -> HomeScreen(onNavigateToDetails = {

            currentScreen.value = Screen.Details
        }, onNavigateToSettings = {
            currentScreen.value = Screen.Settings
        })

        Screen.Details -> LoginScreen(onNavigateBack = {
            currentScreen.value = Screen.Home
        })

        Screen.Settings -> SettingsScreen(onNavigateBack = {
            currentScreen.value = Screen.Home
        })
    }
}



