package ui.server

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ServerView(state: ServerState, viewModel: ServerViewModel) {
    Column {
        Spacer(modifier = Modifier.size(16.dp))

        AdvertiseView(state = state, viewModel = viewModel)

        Spacer(modifier = Modifier.size(16.dp))

        StateView(state = state, viewModel = viewModel)
    }
}
