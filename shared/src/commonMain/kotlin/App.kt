import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import theme.NordicTheme
import ui.main.MainScreen

@Composable
fun App(
    content: @Composable () -> Unit = { Content() }
) {
    NordicTheme {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            content()
        }
    }
}

@Composable
fun Content() {
    Navigator(MainScreen())
}

expect fun getPlatformName(): String
