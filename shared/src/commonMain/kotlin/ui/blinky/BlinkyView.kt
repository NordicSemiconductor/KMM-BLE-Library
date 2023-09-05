package ui.blinky

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BlinkyView(
    ledState: Boolean,
    buttonState: Boolean,
    onStateChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        LedControlView(
            state = ledState,
            onStateChanged = onStateChanged,
        )

        ButtonControlView(
            state = buttonState
        )
    }
}
