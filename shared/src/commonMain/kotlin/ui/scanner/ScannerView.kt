package ui.scanner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import scanner.KMMDevice
import ui.blinky.BlinkyScreen

@Composable
fun ScannerView(devices: List<KMMDevice>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item { Text("Scanner working...") }
        items(devices.size) {
            DeviceView(devices[it])
        }
    }
}

@Composable
fun DeviceView(device: KMMDevice) {
    val navigator = LocalNavigator.currentOrThrow
    DeviceListItem(
        device.name,
        device.address ?: "00:00:00:00:00",
        modifier = Modifier.clickable { navigator.push(BlinkyScreen(device)) }
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
