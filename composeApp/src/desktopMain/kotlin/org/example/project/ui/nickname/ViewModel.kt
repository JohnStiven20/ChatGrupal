package org.example.project.ui.nickname

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.repository.CoroutineDispatchers
import org.example.project.repository.SocketRepository


class ViewModel(
    private val socketRepository: SocketRepository,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    private val _nickName = MutableStateFlow("")
    val nickname: StateFlow<String> = _nickName
    private var _entrada = MutableStateFlow("")
    var entrada: StateFlow<String> = _entrada
    private var _entradaChat = MutableStateFlow("")
    var entradaChat: StateFlow<String> = _entradaChat

    private var _nombreUsuario = MutableStateFlow("")
    var nombreUsuario: StateFlow<String> = _nombreUsuario


    init {
        connection()
    }

    private fun connection(dirrecion: String = "192.168.0.18", puerto: Int = 4444) {
        viewModelScope.launch(dispatchers.io) {

            try {
                socketRepository.connection(dirrecion, puerto)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch() {
            try {
                socketRepository.sendMessage(message)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun recibirMensaje(onMessageReceived: (String) -> Unit) {
        viewModelScope.launch {
            try {
                while (true) {
                    val result = socketRepository.receiveMessage()

                    result.onSuccess { response ->
                        if (!response.isNullOrBlank()) { // Ignora mensajes vacíos

                            val comando = response.substring(0, response.indexOf(",")).uppercase()

                            if (comando == "CHT") {
                                _entradaChat.update { response }
                            } else if (comando == "OK" || comando == "NOK") {
                                _entrada.update { response.uppercase() }
                            } else if (comando == "LST") {
                                _nombreUsuario.update { response}
                            }
                        }
                    }.onFailure { exception ->
                        exception.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun onChangeChat(entrada: String) {
        this._entradaChat.update { entrada }
    }

    fun onChangeEntrada(entrada: String) {
        this._entrada.update { entrada }
    }

    fun closeConnection() {
        viewModelScope.launch() {
            socketRepository.closeConnection()
        }
    }

    fun onChange(nickName: String) {
        this._nickName.update { nickName }
    }

    fun getInnerAddress(): String {
        return socketRepository.getInnerAddress()
    }

}