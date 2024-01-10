/*
 * Copyright (c) 2023, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package server

import no.nordicsemi.android.kotlin.ble.core.data.BleGattPermission
import no.nordicsemi.android.kotlin.ble.core.data.BleGattProperty

internal fun List<BleGattPermission>.toDomainPermissions(): List<GattPermission> {
    return this.mapNotNull {
        when (it) {
            BleGattPermission.PERMISSION_READ -> GattPermission.READ
            BleGattPermission.PERMISSION_WRITE -> GattPermission.WRITE
            BleGattPermission.PERMISSION_READ_ENCRYPTED,
            BleGattPermission.PERMISSION_READ_ENCRYPTED_MITM,
            BleGattPermission.PERMISSION_WRITE_ENCRYPTED,
            BleGattPermission.PERMISSION_WRITE_ENCRYPTED_MITM,
            BleGattPermission.PERMISSION_WRITE_SIGNED,
            BleGattPermission.PERMISSION_WRITE_SIGNED_MITM -> null
        }
    }
}

internal fun List<BleGattProperty>.toDomainProperties(): List<GattProperty> {
    return this.mapNotNull {
        when (it) {
            BleGattProperty.PROPERTY_READ -> GattProperty.READ
            BleGattProperty.PROPERTY_WRITE -> GattProperty.WRITE
            BleGattProperty.PROPERTY_INDICATE -> GattProperty.INDICATE
            BleGattProperty.PROPERTY_NOTIFY -> GattProperty.NOTIFY
            BleGattProperty.PROPERTY_BROADCAST,
            BleGattProperty.PROPERTY_EXTENDED_PROPS,
            BleGattProperty.PROPERTY_SIGNED_WRITE,
            BleGattProperty.PROPERTY_WRITE_NO_RESPONSE -> null
        }
    }
}

internal fun List<GattPermission>.toNativePermissions(): List<BleGattPermission> {
    return this.map {
        when (it) {
            GattPermission.READ -> BleGattPermission.PERMISSION_READ
            GattPermission.WRITE -> BleGattPermission.PERMISSION_WRITE
        }
    }
}

internal fun List<GattProperty>.toNativeProperties(): List<BleGattProperty> {
    return this.map {
        when (it) {
            GattProperty.READ -> BleGattProperty.PROPERTY_READ
            GattProperty.WRITE -> BleGattProperty.PROPERTY_WRITE
            GattProperty.NOTIFY -> BleGattProperty.PROPERTY_NOTIFY
            GattProperty.INDICATE -> BleGattProperty.PROPERTY_INDICATE
        }
    }
}
