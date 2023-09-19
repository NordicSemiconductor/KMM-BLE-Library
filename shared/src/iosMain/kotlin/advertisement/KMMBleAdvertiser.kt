package advertisement

import org.koin.core.component.KoinComponent

actual class KMMBleAdvertiser(private val server: IOSServer) : KoinComponent {

    actual suspend  fun advertise(settings: KMMAdvertisementSettings) {
        server.advertise(settings)
    }

    actual suspend fun stop() {
        server.stopAdvertising()
    }
}
