package scanner

import client.IOSClientWrapper
import kotlinx.coroutines.flow.Flow

actual class KMMScanner(private val client: IOSClientWrapper) {

    actual fun scan(): Flow<List<KMMDevice>> = client.value.scan()
}
