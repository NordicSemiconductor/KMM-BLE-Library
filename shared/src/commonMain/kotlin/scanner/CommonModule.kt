package scanner

import org.koin.dsl.module

val ScannerSharedModule = module {
    single { KMMScannerAggregator(get()) }
}
