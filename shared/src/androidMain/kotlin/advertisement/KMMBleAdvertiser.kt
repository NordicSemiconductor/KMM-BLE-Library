package advertisement

import android.annotation.SuppressLint
import android.content.Context
import android.os.ParcelUuid
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.launchIn
import no.nordicsemi.android.kotlin.ble.advertiser.BleAdvertiser
import no.nordicsemi.android.kotlin.ble.core.advertiser.BleAdvertisingConfig
import no.nordicsemi.android.kotlin.ble.core.advertiser.BleAdvertisingData
import no.nordicsemi.android.kotlin.ble.core.advertiser.BleAdvertisingSettings

actual class KMMBleAdvertiser(private val context: Context) {

    private var job: Job? = null

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

        coroutineScope {
            advertiser.advertise(advertiserConfig).launchIn(this)
        }
    }

    actual suspend fun stop() {
        job?.cancel()
    }
}
