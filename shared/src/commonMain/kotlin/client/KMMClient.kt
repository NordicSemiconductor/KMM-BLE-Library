package client

import scanner.KMMDevice

expect class KMMClient {

    suspend fun connect(device: KMMDevice)

    suspend fun disconnect()

    suspend fun discoverServices(): KMMServices

}

sealed interface DeviceConnectionState

data object DeviceConnected : DeviceConnectionState

data object DeviceDisconnected: DeviceConnectionState


sealed interface OperationStatus

data object OperationSuccess : OperationStatus

data object OperationError : OperationStatus
