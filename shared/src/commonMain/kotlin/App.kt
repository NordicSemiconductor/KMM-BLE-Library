import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.ExperimentalResourceApi
import view.ScannerView

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App(scannerViewModel: ScannerViewModel) {
    MaterialTheme {
        var greetingText by remember { mutableStateOf("Hello, World!") }
        var showImage by remember { mutableStateOf(false) }

        val text = scannerViewModel.value

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
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