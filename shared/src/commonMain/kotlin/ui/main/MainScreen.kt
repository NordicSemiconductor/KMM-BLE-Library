package ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ui.scanner.ScannerScreen
import ui.server.ServerScreen

class MainScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Column {
            Button(onClick = { navigator.push(ScannerScreen()) }) {
                Text("Client")
            }


            Button(onClick = { navigator.push(ServerScreen()) }) {
                Text("Server")
            }
        }
    }
}
