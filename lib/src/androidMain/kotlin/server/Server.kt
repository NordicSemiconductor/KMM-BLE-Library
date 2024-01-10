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

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import no.nordicsemi.android.kotlin.ble.server.main.ServerBleGatt
import no.nordicsemi.android.kotlin.ble.server.main.service.ServerBleGattCharacteristicConfig
import no.nordicsemi.android.kotlin.ble.server.main.service.ServerBleGattDescriptorConfig
import no.nordicsemi.android.kotlin.ble.server.main.service.ServerBleGattServiceConfig
import no.nordicsemi.android.kotlin.ble.server.main.service.ServerBleGattServiceType
import no.nordicsemi.android.kotlin.ble.server.main.service.ServerBluetoothGattConnection
import scanner.IoTDevice

@SuppressLint("MissingPermission")
actual class Server(private val context: Context) {

    private var server: ServerBleGatt? = null

    actual val connections: Flow<Map<IoTDevice, ServerProfile>>
        get() = server?.connections?.map {
            it.mapKeys { IoTDevice(it.key) }.mapValues { it.value.toDomain() }
        } ?: emptyFlow()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    actual suspend fun startServer(
        services: List<BleServerServiceConfig>,
        scope: CoroutineScope,
    ) {

        val config = services.map {
            val characteristics = it.characteristics.map {
                val descritptors = it.descriptors.map {
                    it.uuid
                    ServerBleGattDescriptorConfig(
                        it.uuid,
                        it.permissions.toNativePermissions()
                    )
                }

                ServerBleGattCharacteristicConfig(
                    it.uuid,
                    it.properties.toNativeProperties(),
                    it.permissions.toNativePermissions(),
                    descritptors
                )
            }

            ServerBleGattServiceConfig(
                it.uuid,
                ServerBleGattServiceType.SERVICE_TYPE_PRIMARY,
                characteristics
            )
        }

        server = ServerBleGatt.create(context, scope, *config.toTypedArray())
    }

    actual suspend fun stopServer() {
        server?.stopServer()
    }

    private fun ServerBluetoothGattConnection.toDomain(): ServerProfile {
        return ServerProfile(services)
    }
}
