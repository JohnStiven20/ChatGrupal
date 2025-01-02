package org.example.project.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.repository.SocketRepository


class ChatPersonalViewModel(private val socketRepository: SocketRepository) : ViewModel() {

    private val _nickName = MutableStateFlow("")
    val nickname: StateFlow<String> = _nickName

    fun sendMessage(message: String) {
        viewModelScope.launch() {
            val mensaje = "PRV,$_nickName,$message"
            socketRepository.sendMessage(mensaje)
        }
    }

    fun setOnchangeUser(nombre: String) {
        _nickName.update { nombre }
    }

}