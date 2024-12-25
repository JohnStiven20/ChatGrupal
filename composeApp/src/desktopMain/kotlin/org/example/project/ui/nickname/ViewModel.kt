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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    fun connection(dirrecion: String = "192.168.0.18", puerto: Int = 4444) {
        viewModelScope.launch(dispatchers.io) {
            try {
                socketRepository.connection(dirrecion, puerto)
                _isConnected.value = true
            } catch (e: Exception) {
                e.printStackTrace()
                _isConnected.value = false
            }
        }
    }

    fun sendMessage(message: String, onProgress: (Boolean) -> Unit) {
        viewModelScope.launch() {
            try {
                _isLoading.update { true }
                val result = socketRepository.sendMessage(message)
                result.onSuccess { response ->
                        _entrada.update { response?.trim() ?: "NOK" }
                        onProgress(true)
                }.onFailure { exception ->
                    exception.printStackTrace()
                    _entrada.update { "Error: ${exception.message}" }
                    onProgress(false)
                }

            } finally {
                _isLoading.update { false }
            }
        }
    }


    fun startReceivingMessages() {
        viewModelScope.launch() {
            try {
                while (true) {
                    val result = socketRepository.receiveMessage()
                    result.onSuccess { response ->
                        // Actualizamos la entrada con cualquier mensaje que llegue
                        _entrada.update { response ?: "Mensaje nulo o error" }
                    }.onFailure { exception ->
                        exception.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun closeConnection() {
        viewModelScope.launch(dispatchers.io) {
            socketRepository.closeConnection()
            _isConnected.value = false
        }
    }

    fun onChange(nickName: String) {
        this._nickName.update { nickName }
    }

    fun getInnerAddress(): String {
        return socketRepository.getInnerAddress()
    }


}