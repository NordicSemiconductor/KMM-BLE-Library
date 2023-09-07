package server

import platform.CoreBluetooth.CBAttributePermissions
import platform.CoreBluetooth.CBCharacteristicProperties

fun CBCharacteristicProperties.toProperties(): List<KMMCharacteristicProperty> {
    val result = mutableListOf<KMMCharacteristicProperty>()

    if (this and platform.CoreBluetooth.CBCharacteristicPropertyRead > 0u) {
        result.add(server.KMMCharacteristicProperty.READ)
    }
    if (this and platform.CoreBluetooth.CBCharacteristicPropertyWrite > 0u) {
        result.add(server.KMMCharacteristicProperty.WRITE)
    }
    if (this and platform.CoreBluetooth.CBCharacteristicPropertyNotify > 0u) {
        result.add(server.KMMCharacteristicProperty.NOTIFY)
    }
    if (this and platform.CoreBluetooth.CBCharacteristicPropertyIndicate > 0u) {
        result.add(server.KMMCharacteristicProperty.INDICATE)
    }

    return result.toList()
}

fun CBAttributePermissions.toPermissions(): List<KMMBlePermission> {
    val result = mutableListOf<KMMBlePermission>()

    if (this and platform.CoreBluetooth.CBAttributePermissionsWriteable > 0u) {
        result.add(server.KMMBlePermission.WRITE)
    }
    if (this and platform.CoreBluetooth.CBAttributePermissionsReadable > 0u) {
        result.add(server.KMMBlePermission.READ)
    }

    return result.toList()
}
