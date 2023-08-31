package scanner

import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal class KMMScannerAggregator(private val scanner: KMMScanner) {

    val result = mutableListOf<KMMDevice>()

    fun getDevices(): Flow<List<KMMDevice>> {
        return scanner.scan()
            .onEach { result += it }
            .map { result }
            .map { it.distinctBy { it.address } }
            .catch { Napier.d("Exception occurred", it) }
    }
}
