package server

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import no.nordicsemi.android.kotlin.ble.server.main.ServerBleGatt
import no.nordicsemi.android.kotlin.ble.server.main.service.ServerBleGattCharacteristicConfig
import no.nordicsemi.android.kotlin.ble.server.main.service.ServerBleGattDescriptorConfig
import no.nordicsemi.android.kotlin.ble.server.main.service.ServerBleGattServiceConfig
import no.nordicsemi.android.kotlin.ble.server.main.service.ServerBleGattServiceType
import no.nordicsemi.android.kotlin.ble.server.main.service.ServerBluetoothGattConnection
import scanner.KMMDevice

@SuppressLint("MissingPermission")
actual class KMMBleServer(private val context: Context) {

    private var server: ServerBleGatt? = null

    actual val connections: Flow<Map<KMMDevice, KMMBleServerProfile>>
        get() = server!!.connections.map {
            it.mapKeys { KMMDevice(it.key) }.mapValues { it.value.toDomain() }
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    actual suspend fun startServer(services: List<KMMBleServerServiceConfig>) {

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

        server = ServerBleGatt.create(context, *config.toTypedArray())
    }

    actual suspend fun stopServer() {
        server?.stopServer()
    }

    private fun ServerBluetoothGattConnection.toDomain(): KMMBleServerProfile {
        return KMMBleServerProfile(services)
    }
}
