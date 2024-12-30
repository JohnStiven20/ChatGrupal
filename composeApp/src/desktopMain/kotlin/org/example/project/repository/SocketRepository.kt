package org.example.project.repository

import kotlinx.coroutines.withContext
import org.example.project.Interface.SocketRepositoryInt
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

class SocketRepository(private val dispatchers: CoroutineDispatchers = CoroutineDispatchers()) :
    SocketRepositoryInt {

    private var socket: Socket? = null
    private var entrada: DataInputStream? = null
    private var salida: DataOutputStream? = null

    override suspend fun connection(dirrecion: String, puerto: Int) {
        withContext(dispatchers.main) {
            try {
                socket = Socket(dirrecion, puerto)
                val entradaTemporal = socket?.getInputStream()
                val salidaTemporal = socket?.getOutputStream()

                if (entradaTemporal != null && salidaTemporal != null) {
                    entrada = DataInputStream(entradaTemporal)
                    salida = DataOutputStream(salidaTemporal)
                } else {
                    throw RuntimeException("Error: InputStream o OutputStream es null")
                }

            } catch (e: Exception) {
                throw RuntimeException("Error al conectar al servidor: ${e.message}")
            }
        }

    }

    override suspend fun sendMessage(message: String) {
        return withContext(dispatchers.io) {
                salida?.writeUTF(message)
                salida?.flush()
        }
    }



    override suspend fun receiveMessage(): Result<String?> {
        return withContext(dispatchers.io) {
            try {
                Result.success(entrada?.readUTF())
            }catch (e:Exception){
                Result.failure(e)
            }
        }
    }


    override suspend fun closeConnection() {
        withContext(dispatchers.io) {
            try {
                entrada?.close()
                salida?.close()
                socket?.close()
            } catch (e: Exception) {
                throw RuntimeException("Error al cerrar la conexión: ${e.message}")
            }
        }
    }

    fun getInnerAddress(): String {

        val inetAddress = socket?.inetAddress?.hostAddress

        if (inetAddress != null) {
            return inetAddress
        } else {
            throw RuntimeException("Error: Socket es null")
        }
    }


}