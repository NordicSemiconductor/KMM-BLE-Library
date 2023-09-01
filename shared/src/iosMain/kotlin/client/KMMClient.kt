package client

import kotlinx.coroutines.flow.Flow
import scanner.KMMDevice

@Suppress("CONFLICTING_OVERLOADS")
actual class KMMClient(
    private val client: IOSClientWrapper
) {

    fun scan(): Flow<List<KMMDevice>> {
        return client.value.scan()
    }

    actual suspend fun connect(device: KMMDevice) {
        client.value.connect(device)
    }

    actual suspend fun disconnect() {
        return client.value.disconnect()
    }

    actual suspend fun discoverServices(): KMMServices {
        return client.value.discoverServices()
    }
}
