package scanner

import advertisement.KMMBleAdvertiser
import client.KMMClient
import org.koin.core.module.Module
import org.koin.dsl.module
import server.KMMBleServer

actual val ScannerModule: Module = module {
    single { KMMScanner(get()) }
    single { KMMClient(get()) }
    single { KMMBleServer(get()) }
    single { KMMBleAdvertiser(get()) }
}
