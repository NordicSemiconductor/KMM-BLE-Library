package server

import platform.CoreBluetooth.CBAttributePermissions
import platform.CoreBluetooth.CBAttributePermissionsReadable
import platform.CoreBluetooth.CBAttributePermissionsWriteable
import platform.CoreBluetooth.CBCharacteristicProperties
import platform.CoreBluetooth.CBCharacteristicPropertyIndicate
import platform.CoreBluetooth.CBCharacteristicPropertyNotify
import platform.CoreBluetooth.CBCharacteristicPropertyRead
import platform.CoreBluetooth.CBCharacteristicPropertyWrite

fun CBCharacteristicProperties.toProperties(): List<KMMCharacteristicProperty> {
    val result = mutableListOf<KMMCharacteristicProperty>()

    if (this and CBCharacteristicPropertyRead > 0u) {
        result.add(KMMCharacteristicProperty.READ)
    }
    if (this and CBCharacteristicPropertyWrite > 0u) {
        result.add(KMMCharacteristicProperty.WRITE)
    }
    if (this and CBCharacteristicPropertyNotify > 0u) {
        result.add(KMMCharacteristicProperty.NOTIFY)
    }
    if (this and CBCharacteristicPropertyIndicate > 0u) {
        result.add(KMMCharacteristicProperty.INDICATE)
    }

    return result.toList()
}

fun CBAttributePermissions.toPermissions(): List<KMMBlePermission> {
    val result = mutableListOf<KMMBlePermission>()

    if (this and CBAttributePermissionsWriteable > 0u) {
        result.add(KMMBlePermission.WRITE)
    }
    if (this and CBAttributePermissionsReadable > 0u) {
        result.add(KMMBlePermission.READ)
    }

    return result.toList()
}
