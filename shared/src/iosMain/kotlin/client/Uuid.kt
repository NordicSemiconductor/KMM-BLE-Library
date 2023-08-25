package client

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import platform.CoreBluetooth.CBUUID
import platform.Foundation.NSUUID

internal fun Uuid.toNSUUID(): NSUUID = NSUUID(toString())
internal fun Uuid.toCBUUID(): CBUUID = CBUUID.UUIDWithString(toString())
internal fun CBUUID.toUuid(): Uuid = when (UUIDString.length) {
    4 -> uuidFrom("0000$UUIDString-0000-0000-0000-000000000000")
    else -> uuidFrom(UUIDString)
}

internal fun NSUUID.toUuid(): Uuid = uuidFrom(UUIDString)
