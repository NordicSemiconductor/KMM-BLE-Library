package server

import advertisement.IOSServerWrapper
import kotlinx.coroutines.flow.Flow
import scanner.KMMDevice

actual class KMMBleServer(private val server: IOSServerWrapper) {

    actual val connections: Flow<Map<KMMDevice, KMMBleServerProfile>>
        get() = server.value.connections

    actual suspend fun startServer(services: List<KMMBleServerServiceConfig>) {
        server.value.startServer(services)
    }

    actual suspend fun stopServer() {
        server.value.stopAdvertising()
    }
}
