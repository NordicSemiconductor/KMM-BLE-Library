package scanner

import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class KMMScannerAggregator(private val scanner: KMMScanner) {

    val result = mutableListOf<KMMDevice>()

    fun getDevices(): Flow<List<KMMDevice>> {
        Napier.i("getDevices()", tag = "AAATESTAAA")
        return scanner.scan()
            .onEach { Napier.i("device: ${it.address}", tag = "AAATESTAAA") }
            .onStart { Napier.i("start()", tag = "AAATESTAAA") }
            .onEach { result += it }
            .map {
                Napier.i("device: ${it.address}", tag = "AAATESTAAA")
                result }
    }
}

expect class KMMScanner {

    fun scan(): Flow<KMMDevice>
}

data class KMMDevice(
    val name: String,
    val address: String
)
