package scanner

import org.koin.core.module.Module
import org.koin.dsl.module

actual val ScannerModule: Module = module {
    single { KMMScanner(get()) }
}