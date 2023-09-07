package server

import advertisement.IOSServer
import kotlinx.coroutines.flow.Flow
import scanner.KMMDevice

actual class KMMBleServer(private val server: IOSServer) {

    actual val connections: Flow<Map<KMMDevice, KMMBleServerProfile>>
        get() = TODO("Not yet implemented")

    actual suspend fun startServer(services: List<KMMBleServerServiceConfig>) {
        server.startServer(services)
    }

    actual suspend fun stopServer() {
        server.stop()
    }
}
