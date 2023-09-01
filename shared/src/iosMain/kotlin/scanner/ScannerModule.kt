package scanner

import client.IOSClient
import client.IOSClientWrapper
import client.KMMClient
import org.koin.core.module.Module
import org.koin.dsl.module

actual val ScannerModule: Module = module {
    single { IOSClientWrapper(IOSClient()) }
    single { KMMScanner(get()) }
    single { KMMClient(get()) }
}
