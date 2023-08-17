package view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.compose.koinInject
import scanner.KMMDevice
import scanner.KMMScannerAggregator

@Composable
fun ScannerView() {
    val scanner = koinInject<KMMScannerAggregator>()
    val devices = remember { mutableStateOf(scanner.result.toList()) }

    val scope = rememberCoroutineScope()
    DisposableEffect(Unit) {
        Napier.i("Start launch effect", tag = "BBBTESBBB")
        val job = scanner.getDevices()
            .onEach { devices.value = devices.value + it }
            .launchIn(scope)

        onDispose {
            Napier.i("Dispose", tag = "BBBTESBBB")
            job.cancel()
        }
    }

    DevicesList(devices.value)
}

@Composable
fun DevicesList(devices: List<KMMDevice>) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item { Text("Scanner working...") }
        items(devices.size) {
            DeviceView(devices[it])
        }
    }
}

@Composable
fun DeviceView(device: KMMDevice) {
    Text("Device: ${device.name}")
    Text("Rssi: ${device.address}")
}
