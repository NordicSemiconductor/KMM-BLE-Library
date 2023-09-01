package client

import platform.CoreBluetooth.CBPeripheral
import platform.Foundation.NSData
import platform.Foundation.NSError

internal sealed interface IOSGattEvent

internal sealed interface CharacteristicEvent : IOSGattEvent

internal data class OnGattCharacteristicWrite(
    val peripheral: CBPeripheral,
    val data: NSData?,
    val error: NSError?
) : CharacteristicEvent

internal data class OnGattCharacteristicRead(
    val peripheral: CBPeripheral,
    val data: NSData?,
    val error: NSError?
) : CharacteristicEvent

internal sealed interface DescriptorEvent : CharacteristicEvent

internal data class OnGattDescriptorWrite(
    val peripheral: CBPeripheral,
    val data: NSData?,
    val error: NSError?
) : DescriptorEvent

internal data class OnGattDescriptorRead(
    val peripheral: CBPeripheral,
    val data: NSData?,
    val error: NSError?
) : DescriptorEvent
