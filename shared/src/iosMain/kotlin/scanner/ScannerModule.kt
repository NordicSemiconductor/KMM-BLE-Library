package scanner

import advertisement.IOSServer
import advertisement.KMMBleAdvertiser
import client.IOSClient
import client.IOSClientWrapper
import client.KMMClient
import org.koin.core.module.Module
import org.koin.dsl.module
import server.KMMBleServer
import server.NotificationsRecords

actual val ScannerModule: Module = module {
    single { IOSClientWrapper(IOSClient()) }
    single { KMMScanner(get()) }
    single { KMMClient(get()) }
    single { NotificationsRecords() }
    single { KMMBleServer(IOSServer(get())) }
    single { KMMBleAdvertiser(get()) }
}
