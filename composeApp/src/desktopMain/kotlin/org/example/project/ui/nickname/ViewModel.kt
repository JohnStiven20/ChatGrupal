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

    private val _nickNamePrivado = MutableStateFlow("")
    val nicknamePrivado: StateFlow<String> = _nickNamePrivado

     private val _mapaUsuarios = MutableStateFlow<MutableMap<String, MutableList<Mensaje>>>(mutableMapOf())
     val mapaUsuarios: StateFlow<MutableMap<String, MutableList<Mensaje>>> = _mapaUsuarios

    private val _nickName1 = MutableStateFlow("")
    val nickname1: StateFlow<String> = _nickName1

    private val _nickName2 = MutableStateFlow("")
    val nickname2: StateFlow<String> = _nickName2


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

    fun sendMessagePrivado(message: String) {
        viewModelScope.launch() {
            socketRepository.sendMessage(message)
        }
    }

    fun setOnchangeUser(nombre: String) {
        _nickNamePrivado.update { nombre }
    }

    fun recibirMensaje(onMessageReceived: (String) -> Unit) {
        viewModelScope.launch {
            try {
                while (true) {
                    val result = socketRepository.receiveMessage()

                    result.onSuccess { response ->

                        if (!response.isNullOrBlank()) { // Ignora mensajes vacíos
                            println("Mensaje recibido en el Metodo recibirMensaje: $response")
                            val comando = response.substring(0, response.indexOf(",")).uppercase()

                            if (comando == "CHT") {
                                _entradaChat.update { response }
                            } else if (comando == "OK" || comando == "NOK") {
                                _entrada.update { response.uppercase() }
                            } else if (comando == "LST") {
                                _nombreUsuario.update {response}
                            } else if (comando.uppercase() == "PRV") {

                                val nombre = response.substring(
                                    response.indexOf(",") + 1,
                                    response.lastIndexOf(",")
                                )

                                val mensaje = response.substring(
                                    response.lastIndexOf(",") + 1,
                                    response.length
                                )

                                onAtPrivado(nombre, Mensaje(usario = nombre, mensaje = mensaje) );
                            } else if (comando == "POK") {

                            } else if (comando == "NOP") {

                            } else if (comando == "EXIT") {

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

    private fun onAtPrivado(key: String, mensaje: Mensaje) {
        _mapaUsuarios.update { currentMap ->
            val updatedMap = currentMap.toMutableMap() // Crea una copia inmutable
            val mensajes = updatedMap[key]?.toMutableList() ?: mutableListOf()
            mensajes.add(mensaje)
            updatedMap[key] = mensajes
            println("Mapa emitido al flujo: $updatedMap") // LOG para verificar emisión
            updatedMap // Retorna el nuevo mapa
        }
    }


    fun addMap(clave: String, valor: Mensaje) {

        _mapaUsuarios.update { currentMap ->
            val updatedMap = currentMap.toMutableMap() // Crea una copia inmutable
            val mensajes = updatedMap[clave]?.toMutableList() ?: mutableListOf()
            mensajes.add(valor)
            updatedMap[clave] = mensajes
            println("Mapa emitido al flujo: $updatedMap") // LOG para verificar emisión
            updatedMap // Retorna el nuevo mapa
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

    fun onChange1(nickName: String) {
        _nickName2.update { nickName }
    }

    fun getInnerAddress(): String {
        return socketRepository.getInnerAddress()
    }

}