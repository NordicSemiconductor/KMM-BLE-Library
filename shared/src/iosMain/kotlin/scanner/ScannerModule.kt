package scanner

import client.IOSClient
import client.KMMClient
import org.koin.core.module.Module
import org.koin.dsl.module

actual val ScannerModule: Module = module {
    single { KMMScanner() }
    single { KMMClient(IOSClient()) }
}
