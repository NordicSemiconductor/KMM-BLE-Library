package client

import android.annotation.SuppressLint
import android.content.Context
import no.nordicsemi.android.kotlin.ble.client.main.callback.ClientBleGatt
import scanner.KMMDevice

@SuppressLint("MissingPermission")
actual class KMMClient(
    private val context: Context
) {

    private var client: ClientBleGatt? = null

    actual suspend fun connect(device: KMMDevice) {
        client = ClientBleGatt.connect(context, device.device)
    }

    actual suspend fun disconnect() {
        client?.disconnect()
    }

    actual suspend fun discoverServices(): KMMServices {
        return client!!.discoverServices().toKMM()
    }
}
