package advertisement

import org.koin.core.component.KoinComponent

actual class KMMBleAdvertiser(private val server: IOSServerWrapper) : KoinComponent {

    actual suspend  fun advertise(settings: KMMAdvertisementSettings) {
        server.value.advertise(settings)
    }

    actual suspend fun stop() {
        server.value.stopAdvertising()
    }
}
