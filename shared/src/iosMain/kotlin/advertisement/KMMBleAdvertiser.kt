package advertisement

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class KMMBleAdvertiser : KoinComponent {

    private val server: IOSServer by inject()

    actual suspend  fun advertise(settings: KMMAdvertisementSettings) {
        server.advertise(settings)
    }

    actual suspend fun stop() {
        server.stop()
    }
}
