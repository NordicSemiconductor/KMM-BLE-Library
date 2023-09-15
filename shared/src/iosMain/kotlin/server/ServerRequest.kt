package server

import platform.CoreBluetooth.CBATTRequest

sealed interface ServerRequest

data class ReadRequest(val request: CBATTRequest) : ServerRequest

data class WriteRequest(val request: List<CBATTRequest>) : ServerRequest
