package ui.server

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import theme.NordicColors

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AdvertiseView(state: ServerState, viewModel: ServerViewModel) {
    OutlinedCard(modifier = Modifier.padding(horizontal = 16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            SectionTitle(
                painter = painterResource("ic_broadcast.xml"),
                title = ServerStrings.characteristics
            )

            Spacer(modifier = Modifier.size(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                val icon = if (state.isAdvertising) {
                    painterResource("ic_advertisements")
                } else {
                    painterResource("ic_advertisements_off")
                }

                val color = if (state.isAdvertising) {
                    NordicColors.green
                } else {
                    NordicColors.gray
                }

                Icon(
                    painter = icon,
                    contentDescription = "",
                    tint = color,
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.size(16.dp))

                Text(
                    text = ServerStrings.advertisement_state + state.isAdvertising.toDisplayString(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.size(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))

                if (state.isAdvertising) {
                    Button(
                        onClick = { viewModel.stopAdvertise() },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Text(ServerStrings.stop)
                    }
                } else {
                    Button(
                        onClick = { viewModel.advertise() },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Text(ServerStrings.advertise)
                    }
                }
            }
        }
    }
}

@Composable
fun Boolean.toDisplayString(): String {
    return when (this) {
        true -> ServerStrings.state_on
        false -> ServerStrings.state_off
    }
}