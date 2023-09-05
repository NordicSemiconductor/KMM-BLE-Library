package advertisement

import android.annotation.SuppressLint
import android.content.Context
import android.os.ParcelUuid
import no.nordicsemi.android.kotlin.ble.advertiser.BleAdvertiser
import no.nordicsemi.android.kotlin.ble.core.advertiser.BleAdvertisingConfig
import no.nordicsemi.android.kotlin.ble.core.advertiser.BleAdvertisingData
import no.nordicsemi.android.kotlin.ble.core.advertiser.BleAdvertisingSettings

actual class KMMBleAdvertiser(private val context: Context) {

    @SuppressLint("MissingPermission")
    actual suspend fun advertise(settings: KMMAdvertisementSettings) {
        val advertiser = BleAdvertiser.create(context)

        val advertiserConfig = BleAdvertisingConfig(
            settings = BleAdvertisingSettings(
                deviceName = settings.name
            ),
            advertiseData = BleAdvertisingData(
                ParcelUuid(settings.uuid),
            )
        )

        advertiser.advertise(advertiserConfig)
    }
}
