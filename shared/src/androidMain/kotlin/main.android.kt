import androidx.compose.runtime.Composable
import no.nordicsemi.android.common.permissions.ble.RequireBluetooth
import no.nordicsemi.android.common.permissions.ble.RequireLocation

actual fun getPlatformName(): String = "Android"

@Composable
fun MainView() {
    App {
        RequireBluetooth {
            RequireLocation {
                Content()
            }
        }
    }
}
