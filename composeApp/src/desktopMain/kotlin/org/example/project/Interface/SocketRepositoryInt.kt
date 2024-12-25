package org.example.project.Interface

interface SocketRepositoryInt {

    suspend fun connection(dirrecion:String = "localhost", puerto:Int = 4444)
    suspend fun sendMessage(message:String): Result<String?>
    suspend fun receiveMessage(): Result<String?>
    suspend fun closeConnection()

}