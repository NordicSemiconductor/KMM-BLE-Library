package server

import kotlinx.coroutines.flow.Flow
import scanner.KMMDevice

expect class KMMBleServer {

    val connections: Flow<Map<KMMDevice, KMMBleServerProfile>>

    suspend fun startServer(services: List<KMMBleServerServiceConfig>)

    suspend fun stopServer()
}
