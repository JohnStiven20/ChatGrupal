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
        println(message + " VIVA ESPAÑA")
        return withContext(dispatchers.io) {
            try {
                if (socket?.isClosed == false && salida != null) {
                    salida!!.writeUTF(message)
                    salida!!.flush()
                } else {
                    println("Error: Intento de escribir en un socket cerrado.")
                }
            } catch (e: Exception) {
                println("Error al enviar mensaje: ${e.message}")
            }
        }
    }


    suspend fun receiveMessage(): Result<String?> {
        return withContext(dispatchers.io) {
            try {
                if (entrada != null) {
                    Result.success(entrada!!.readUTF())
                } else {
                    Result.failure(Exception("La conexión está cerrada."))
                }
            } catch (e: Exception) {
                println("Error al recibir mensaje: ${e.message}")
                Result.failure(e)
            }
        }
    }



    fun closeConnection() {
        try {
            if (socket != null && !socket!!.isClosed) {
                entrada?.close()
                salida?.close()
                socket?.close()
                estadoConexion = false
                println("Conexión cerrada correctamente.")
            } else {
                println("El socket ya estaba cerrado.")
            }
        } catch (e: Exception) {
            println("Error al cerrar la conexión: ${e.message}")
        }
    }

}