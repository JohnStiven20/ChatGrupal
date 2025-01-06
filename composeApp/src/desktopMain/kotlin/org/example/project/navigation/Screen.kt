package org.example.project.navigation

sealed class ScreenExperimental (
    val route:String
){
    object Registro : ScreenExperimental("registro")
    object Login : ScreenExperimental("login")
    object Home : ScreenExperimental("Home")
}

enum class Screen {
    NickName,
    ChatGeneral,
    ChatPrivado
}
