package scanner

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.compose.koinInject

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
    DeviceListItem(
        device.name,
        device.address ?: "00:00:00:00:00"
    )
}

@Composable
fun DeviceListItem(
    name: String?,
    address: String,
    modifier: Modifier = Modifier,
    extras: @Composable () -> Unit = {},
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically)
    {
        CircularIcon(Icons.Default.Phone)

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            name?.let { name ->
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium
                )
            } ?: Text(
                text = "Unknown",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.alpha(0.7f)
            )
            Text(
                text = address,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        extras()
    }
}

@Composable
fun CircularIcon(
    painter: ImageVector,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.secondary,
    enabled: Boolean = true,
) {
    Image(
        imageVector = painter,
        contentDescription = null,
        colorFilter = if (enabled) {
            ColorFilter.tint(MaterialTheme.colorScheme.contentColorFor(backgroundColor))
        } else {
            ColorFilter.tint(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f))
        },
        modifier = modifier
            .background(
                color = if (enabled) {
                    backgroundColor
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                },
                shape = CircleShape
            )
            .padding(8.dp)
    )
}
