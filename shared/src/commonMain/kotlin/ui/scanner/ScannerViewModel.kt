package ui.scanner

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import scanner.KMMDevice
import scanner.KMMScanner

class ScannerViewModel : ScreenModel, KoinComponent {

    private val scanner: KMMScanner by inject()

    private val _state = MutableStateFlow(emptyList<KMMDevice>())
    val state = _state.asStateFlow()

    init {
        scanner.scan()
            .onEach { _state.value = (_state.value + it).distinctBy { it.address } }
            .launchIn(coroutineScope)
    }
}
