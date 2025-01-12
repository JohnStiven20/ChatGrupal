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

    private var _listaUsarios = MutableStateFlow("")
    var listaUsuarios: StateFlow<String> = _listaUsarios

    private val _nickNamePrivado = MutableStateFlow("")
    val nicknamePrivado: StateFlow<String> = _nickNamePrivado

    private val _mapaUsuarios = MutableStateFlow<MutableMap<String, MutableList<Mensaje>>>(mutableMapOf())
    val mapaUsuarios: StateFlow<MutableMap<String, MutableList<Mensaje>>> = _mapaUsuarios

    private val _nickName1 = MutableStateFlow("")
    val nickname1: StateFlow<String> = _nickName1

    private val _estadoConexion = MutableStateFlow(true)
    val estadoConexion: StateFlow<Boolean> = _estadoConexion

    private val _listaMensajes =MutableStateFlow<MutableList<String>>(mutableListOf())
    val listaMensajes:StateFlow<MutableList<String>> = _listaMensajes

    fun connection(
        dirrecion: String = "192.168.0.18",
        puerto: Int = 4444,
        onConnected: (Boolean) -> Unit
    ) {
        viewModelScope.launch(dispatchers.io) {
            try {
                val estadoConectado = socketRepository.connection(dirrecion, puerto)

                if (estadoConectado) {
                    onConnected(true)
                } else {
                    onConnected(false)
                }
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

    fun encender() {
        _estadoConexion.update {true}
    }

    fun sendMessagePrivado(message: String) {
        viewModelScope.launch() {
            socketRepository.sendMessage(message)
        }
    }

    fun setCambioUsuario(nombre: String) {
        _nickNamePrivado.update { nombre }
    }

    fun recibirMensajeServidor() {

        viewModelScope.launch {

            try {
                while (_estadoConexion.value) {
                    val result = socketRepository.receiveMessage()
                    result.onSuccess { response ->

                        if (!response.isNullOrBlank()) {
                            println("Mensaje recibido en el Metodo recibirMensaje: $response")
                            val comando = response.substring(0, response.indexOf(",")).uppercase()

                            if (comando == "CHT") {
                                _entradaChat.update { response }
                                agregarMensaje(response)
                            } else if (comando == "OK" || comando == "NOK") {
                                println("Entrada en recibirMensaje ESPAÑA: $response")
                                _entrada.update { response.uppercase() }
                            } else if (comando == "LST") {
                                _listaUsarios.update { response }
                            } else if (comando.uppercase() == "PRV") {

                                val nombre = response.substring(
                                    response.indexOf(",") + 1,
                                    response.lastIndexOf(",")
                                )

                                val mensaje = response.substring(
                                    response.lastIndexOf(",") + 1,
                                    response.length
                                )

                                onAtPrivado(nombre, Mensaje(usario = nombre, mensaje = mensaje));
                            } else if (comando == "POK") {

                            } else if (comando == "NOP") {

                            } else if (comando == "EXI") {
                                println("Ha entrado en EXI")
                                val usuario = response.substring(response.lastIndexOf(",")+ 1, response.length)
                                println("$usuario esconparado con ${_nickName.value}")
                                if (usuario == _nickName.value) {
                                    println("Usuario $usuario ha salido del chat")
                                    _nickName.update {""}
                                    closeConnection()
                                    _estadoConexion.update { false }
                                } else {
                                    sendMessage("LUS,")
                                }
                            }
                        }
                    }.onFailure { exception ->
                        println("Error al recibir el mensaje: ${exception.message}")
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

    private fun agregarMensaje(mensaje: String) {
        _listaMensajes.update { currentList ->
            val updatedList = currentList.toMutableList()
            updatedList.add(mensaje)
            updatedList
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
        socketRepository.closeConnection()
    }

    fun onChange(nickName: String) {
        this._nickName.update { nickName }
    }

    fun resetStates() {
        _entrada.value = ""
        _entradaChat.value = ""
        _listaUsarios.value = ""
        _nickNamePrivado.value = ""
        _mapaUsuarios.value = mutableMapOf()
        _estadoConexion.update { true }
    }

    fun getInnerAddress(): String {
        return socketRepository.getInnerAddress()
    }

}