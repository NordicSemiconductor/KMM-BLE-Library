import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.juul.kable.Advertisement
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App(scannerViewModel: ScannerViewModel) {
    MaterialTheme {
        var greetingText by remember { mutableStateOf("Hello, World!") }
        var showImage by remember { mutableStateOf(false) }

        val items = remember { mutableStateOf(emptyList<Advertisement>()) }
        val text = scannerViewModel.value

        val scope = rememberCoroutineScope()

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {

                greetingText = "Hello, ${getPlatformName()}, $text"
                showImage = !showImage
                scannerViewModel.scanForDevices()
                    .onEach { items.value = items.value + it }
                    .catch { it.printStackTrace() }
                    .launchIn(scope)
            }) {
                Text(greetingText)
            }

            LazyColumn {
                items(items.value.size) {
                    val device = items.value[it]
                    Text("Device: ${device.name}")
                    Text("Rssi: ${device.rssi}")
                }
            }
//            AnimatedVisibility(showImage) {
//                Image(
//                    painterResource("compose-multiplatform.xml"),
//                    null
//                )
//            }
        }
    }
}

expect fun getPlatformName(): String