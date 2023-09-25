package ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.icerock.moko.resources.compose.stringResource
import no.nordicsemi.kmm.ble.MR
import ui.scanner.ScannerScreen
import ui.server.ServerScreen

class MainScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Column {
            Button(onClick = { navigator.push(ScannerScreen()) }) {
                Text(stringResource(MR.strings.client))
            }


            Button(onClick = { navigator.push(ServerScreen()) }) {
                Text(stringResource(MR.strings.server))
            }
        }
    }
}
