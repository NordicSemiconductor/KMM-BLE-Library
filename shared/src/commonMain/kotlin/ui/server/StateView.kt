package ui.server

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import theme.NordicColors

@Composable
fun StateView(state: ServerState, viewModel: ServerViewModel) {
    OutlinedCard(modifier = Modifier.padding(horizontal = 16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            SectionTitle(
                icon = Icons.Default.Bluetooth,
                title = ServerStrings.characteristics
            )

            val interactionSource = remember { MutableInteractionSource() }
            val isPressed = interactionSource.collectIsPressedAsState().value

            Spacer(modifier = Modifier.size(16.dp))

            val color = if (state.isLedOn) {
                NordicColors.yellow
            } else {
                NordicColors.gray
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = "",
                    tint = color,
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.size(16.dp))

                Text(
                    text = ServerStrings.led_state + state.isLedOn.toDisplayString(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.size(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = ServerStrings.button_state + state.isButtonPressed.toDisplayString(),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { },
                    interactionSource = interactionSource
                ) {
                    Text(ServerStrings.button)
                }
            }

            LaunchedEffect(isPressed) {
                viewModel.onButtonPressedChanged(isPressed)
            }
        }
    }
}
