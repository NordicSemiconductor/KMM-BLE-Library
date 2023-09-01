package di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import scanner.ScannerModule

fun initKoin(appDeclaration: KoinAppDeclaration) = startKoin {
    appDeclaration()
    modules(
        ScannerModule,
    )
}

fun initKoin() = initKoin {}