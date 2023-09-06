package server

expect class KMMBleServer {

    suspend fun startServer(services: List<KMMBleServerService>)

    suspend fun stopServer()
}
