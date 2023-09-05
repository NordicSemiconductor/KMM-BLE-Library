package ui.server

data class ServerState(
    val isAdvertising: Boolean = false,
    val isLedOn: Boolean = false,
    val isButtonPressed: Boolean = false,
)
