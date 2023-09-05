package ui.server

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen

class ServerScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { ServerViewModel() }
        val state = viewModel.state.collectAsState()

        ServerView(state.value, viewModel)
    }
}

