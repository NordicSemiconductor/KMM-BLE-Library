import com.juul.kable.Advertisement
import com.juul.kable.Scanner
import com.juul.kable.logs.Logging
import com.juul.kable.logs.SystemLogEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import scanner.KMMScanner

class ScannerViewModel(private val scanner: KMMScanner) {

    val scope = CoroutineScope(SupervisorJob())
//    val advertisement: Flow<Advertisement?>

    val value = "aaa"

    fun scanForDevices(): Flow<Advertisement> {
        val scanner = Scanner() {
            filters = null
            logging {
                engine = SystemLogEngine
                level = Logging.Level.Warnings
                format = Logging.Format.Multiline
            }
        }
        return scanner.advertisements
    }
}
