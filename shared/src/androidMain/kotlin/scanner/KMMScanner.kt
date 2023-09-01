package scanner

import android.annotation.SuppressLint
import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import no.nordicsemi.android.kotlin.ble.scanner.BleScanner

actual class KMMScanner(private val context: Context) {

    @SuppressLint("MissingPermission")
    actual fun scan(): Flow<List<KMMDevice>> {
        val scanner = BleScanner(context)

        val result = mutableListOf<KMMDevice>()

        return scanner.scan().map { KMMDevice(it.device) }
            .onEach { result += it }
            .map { result }
            .map { it.distinctBy { it.address } }
    }
}
