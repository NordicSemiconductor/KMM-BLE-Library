package ui.main

import NordicAppBar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import consts.StringConst
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.scanner.ScannerScreen
import ui.server.ServerScreen

class MainScreen : Screen {

    @OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val isClientSelected = remember { mutableStateOf(true) }

        Scaffold(
            topBar = { NordicAppBar(StringConst.MAIN_SCREEN) },
            floatingActionButton = {
                ExtendedFloatingActionButton(onClick = {
                    if (isClientSelected.value) {
                        navigator.push(ScannerScreen())
                    } else {
                        navigator.push(ServerScreen())
                    }
                }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(StringConst.START, style = MaterialTheme.typography.labelLarge)

                        Icon(
                            painter = painterResource("play.xml"),
                            contentDescription = ""
                        )
                    }
                }
            }
        ) {
            Column(modifier = Modifier.padding(it).padding(16.dp)) {

                ChooseYourSideView(
                    icon = painterResource("phone.xml"),
                    info = StringConst.CLIENT,
                    isSelected = isClientSelected.value,
                    selectAction = { isClientSelected.value = true },
                )

                Spacer(modifier = Modifier.size(16.dp))

                ChooseYourSideView(
                    icon = painterResource("dk.xml"),
                    info = StringConst.SERVER,
                    isSelected = !isClientSelected.value,
                    selectAction = { isClientSelected.value = false },
                )
            }
        }
    }
}
