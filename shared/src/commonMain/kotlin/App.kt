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
import scanner.ScannerView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(scannerViewModel: ScannerViewModel) {
    MaterialTheme {
        var greetingText by remember { mutableStateOf("Hello, World!") }
        var showImage by remember { mutableStateOf(false) }

        val text = scannerViewModel.value

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            TopAppBar({ Text("nRF Blinky") })

            Button(onClick = {

                greetingText = "Hello, ${getPlatformName()}, $text"
                showImage = !showImage
            }) {
                Text(greetingText)
            }

            if (showImage) {
                ScannerView()
            }
        }
    }
}

expect fun getPlatformName(): String