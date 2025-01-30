package org.example.project.repository

import kotlinx.coroutines.withContext
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

class SocketRepository(private val dispatchers: CoroutineDispatchers = CoroutineDispatchers()) {

    private var socket: Socket? = null
    private var entrada: DataInputStream? = null
    private var salida: DataOutputStream? = null
    private var estadoConexion: Boolean = false

    suspend fun connection(dirrecion: String, puerto: Int): Boolean {

        return withContext(dispatchers.main) {
            try {
                if (!estadoConexion) {
                    socket = Socket(dirrecion, puerto)
                    val entradaTemporal = socket?.getInputStream()
                    val salidaTemporal = socket?.getOutputStream()

                    if (entradaTemporal != null && salidaTemporal != null) {
                        entrada = DataInputStream(entradaTemporal)
                        salida = DataOutputStream(salidaTemporal)
                        estadoConexion = true
                    } else {
                        throw RuntimeException("Error: InputStream o OutputStream es null")
                    }
                    true
                } else {
                    true
                }

            } catch (e: Exception) {
                estadoConexion = false
                throw RuntimeException("Error al conectar al servidor: ${e.message}")
            }
        }

    }

    suspend fun sendMessage(message: String) {
        return withContext(dispatchers.io) {
            try {
                salida?.writeUTF(message)
                salida?.flush()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun receiveMessage(): Result<String?> {
        return withContext(dispatchers.io) {
            try {
                Result.success(entrada?.readUTF())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }


    fun closeConnection() {
        try {
            entrada?.close()
            salida?.close()
            socket?.close()
            estadoConexion = false

        } catch (e: Exception) {
            throw RuntimeException("Error al cerrar la conexión: ${e.message}")
        }
    }
}