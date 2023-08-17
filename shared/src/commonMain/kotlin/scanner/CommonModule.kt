package scanner

import org.koin.dsl.module

val CommonModule = module {
    single { KMMScannerAggregator(get()) }
}
