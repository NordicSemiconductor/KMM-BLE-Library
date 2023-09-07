package server

import no.nordicsemi.android.kotlin.ble.core.data.BleGattPermission
import no.nordicsemi.android.kotlin.ble.core.data.BleGattProperty

internal fun List<BleGattPermission>.toDomain(): List<KMMBlePermission> {
    return this.mapNotNull {
        when (it) {
            BleGattPermission.PERMISSION_READ -> KMMBlePermission.READ
            BleGattPermission.PERMISSION_WRITE -> KMMBlePermission.WRITE
            BleGattPermission.PERMISSION_READ_ENCRYPTED,
            BleGattPermission.PERMISSION_READ_ENCRYPTED_MITM,
            BleGattPermission.PERMISSION_WRITE_ENCRYPTED,
            BleGattPermission.PERMISSION_WRITE_ENCRYPTED_MITM,
            BleGattPermission.PERMISSION_WRITE_SIGNED,
            BleGattPermission.PERMISSION_WRITE_SIGNED_MITM -> null
        }
    }
}

internal fun List<BleGattProperty>.toDomain(): List<KMMCharacteristicProperty> {
    return this.mapNotNull {
        when (it) {
            BleGattProperty.PROPERTY_READ -> KMMCharacteristicProperty.READ
            BleGattProperty.PROPERTY_WRITE -> KMMCharacteristicProperty.WRITE
            BleGattProperty.PROPERTY_INDICATE -> KMMCharacteristicProperty.INDICATE
            BleGattProperty.PROPERTY_NOTIFY -> KMMCharacteristicProperty.NOTIFY
            BleGattProperty.PROPERTY_BROADCAST,
            BleGattProperty.PROPERTY_EXTENDED_PROPS,
            BleGattProperty.PROPERTY_SIGNED_WRITE,
            BleGattProperty.PROPERTY_WRITE_NO_RESPONSE -> null
        }
    }
}

internal fun List<KMMBlePermission>.toNative(): List<BleGattPermission> {
    return this.map {
        when (it) {
            KMMBlePermission.READ -> BleGattPermission.PERMISSION_READ
            KMMBlePermission.WRITE -> BleGattPermission.PERMISSION_WRITE
        }
    }
}

internal fun List<KMMCharacteristicProperty>.toNative(): List<BleGattProperty> {
    return this.map {
        when (it) {
            KMMCharacteristicProperty.READ -> BleGattProperty.PROPERTY_READ
            KMMCharacteristicProperty.WRITE -> BleGattProperty.PROPERTY_WRITE
            KMMCharacteristicProperty.NOTIFY -> BleGattProperty.PROPERTY_NOTIFY
            KMMCharacteristicProperty.INDICATE -> BleGattProperty.PROPERTY_INDICATE
        }
    }
}
