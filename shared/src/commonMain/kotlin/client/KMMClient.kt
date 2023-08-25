package client

expect class KMMClient {

    suspend fun connect()

    suspend fun disconnect()

    suspend fun discoverServices(): KMMServices

}
