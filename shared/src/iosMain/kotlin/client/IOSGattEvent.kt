package client

import platform.CoreBluetooth.CBPeripheral
import platform.Foundation.NSData

internal sealed interface IOSGattEvent

internal sealed interface CharacteristicEvent : IOSGattEvent

internal data class OnGattCharacteristicWrite(
    val peripheral: CBPeripheral,
    val data: NSData
) : CharacteristicEvent

internal data class OnGattCharacteristicRead(
    val peripheral: CBPeripheral,
    val data: NSData
) : CharacteristicEvent

internal sealed interface DescriptorEvent : CharacteristicEvent

internal data class OnGattDescriptorWrite(
    val peripheral: CBPeripheral,
    val data: NSData
) : DescriptorEvent

internal data class OnGattDescriptorRead(
    val peripheral: CBPeripheral,
    val data: NSData
) : DescriptorEvent
