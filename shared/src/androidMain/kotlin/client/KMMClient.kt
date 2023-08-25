package client

import android.annotation.SuppressLint
import android.content.Context
import no.nordicsemi.android.kotlin.ble.client.main.callback.ClientBleGatt
import no.nordicsemi.android.kotlin.ble.core.ServerDevice

@SuppressLint("MissingPermission")
actual class KMMClient(
    private val context: Context,
    private val device: ServerDevice
) {

    private var client: ClientBleGatt? = null

    actual suspend fun connect() {
        client = ClientBleGatt.connect(context, device)
    }

    actual suspend fun disconnect() {
        client?.disconnect()
    }

    actual suspend fun discoverServices(): KMMServices {
        return client!!.discoverServices().toKMM()
    }
}
