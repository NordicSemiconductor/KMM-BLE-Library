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

package client

import com.benasher44.uuid.Uuid
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBPeripheral
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class ClientDescriptor(
    private val peripheral: CBPeripheral,
    private val native: CBDescriptor,
) {

    val uuid: Uuid = native.UUID.toUuid()

    private var onDescriptorWrite: ((OnGattDescriptorWrite) -> Unit)? = null
    private var onDescriptorRead: ((OnGattDescriptorRead) -> Unit)? = null

    internal fun onEvent(event: DescriptorEvent) {
        when (event) {
            is OnGattDescriptorRead -> onDescriptorRead?.invoke(event)
            is OnGattDescriptorWrite -> onDescriptorWrite?.invoke(event)
        }
    }

    actual suspend fun write(value: ByteArray) {
        return suspendCoroutine { continuation ->
            onDescriptorWrite = {
                onDescriptorWrite = null
                continuation.resume(Unit)
            }
            peripheral.writeValue(value.toNSData(), native)
        }
    }

    actual suspend fun read(): ByteArray {
        return suspendCoroutine { continuation ->
            onDescriptorRead = {
                onDescriptorRead = null
                continuation.resume(it.data?.toByteArray() ?: byteArrayOf())
            }
            peripheral.readValueForDescriptor(native)
        }
    }
}
