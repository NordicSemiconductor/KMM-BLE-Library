package di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import scanner.ScannerModule
import scanner.ScannerSharedModule

fun initKoin(appDeclaration: KoinAppDeclaration) = startKoin {
    appDeclaration()
    modules(
        ScannerModule,
        ScannerSharedModule,
    )
}

fun initKoin() = initKoin {}