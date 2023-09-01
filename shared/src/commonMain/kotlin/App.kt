import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import scanner.ScannerScreen
import theme.NordicTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    content: @Composable () -> Unit = { Content() }
) {
    NordicTheme {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            NordicAppBar("nRF Blinky")

            content()
        }
    }
}

@Composable
private fun Content() {
    Navigator(ScannerScreen())
}

expect fun getPlatformName(): String
