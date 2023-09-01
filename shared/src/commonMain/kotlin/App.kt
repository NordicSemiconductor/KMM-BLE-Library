import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import scanner.ScannerScreen
import scanner.ScannerView
import theme.NordicTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    content: @Composable () -> Unit = { Conent() }
) {
    NordicTheme {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            NordicAppBar("nRF Blinky")

            content()
        }
    }
}

@Composable
fun Conent() {

    var greetingText by remember { mutableStateOf("Hello, World!") }
    var showImage by remember { mutableStateOf(false) }

    Button(onClick = {
        greetingText = "Hello, ${getPlatformName()}"
        showImage = !showImage
    }) {
        Text(greetingText)
    }

    if (showImage) {
        Navigator(ScannerScreen())
    }
}

expect fun getPlatformName(): String