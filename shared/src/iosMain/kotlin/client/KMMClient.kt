package client

import scanner.KMMDevice

@Suppress("CONFLICTING_OVERLOADS")
actual class KMMClient(private val client: IOSClient) {

    actual suspend fun connect(device: KMMDevice) {
        client.connect(device)
    }

    actual suspend fun disconnect() {
        return client.disconnect()
    }

    actual suspend fun discoverServices(): KMMServices {
        return client.discoverServices()
    }
}
