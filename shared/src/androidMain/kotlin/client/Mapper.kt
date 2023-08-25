package client

import no.nordicsemi.android.kotlin.ble.client.main.service.ClientBleGattServices

internal fun ClientBleGattServices.toKMM(): KMMServices {
    return KMMServices(this)
}
