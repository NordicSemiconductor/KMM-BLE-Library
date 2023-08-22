package scanner

import android.annotation.SuppressLint
import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import no.nordicsemi.android.kotlin.ble.scanner.BleScanner

actual class KMMScanner(private val context: Context) {

    @SuppressLint("MissingPermission")
    actual fun scan(): Flow<KMMDevice> {
        val scanner = BleScanner(context)

        return scanner.scan().map { KMMDevice(it.device) }
    }
}
