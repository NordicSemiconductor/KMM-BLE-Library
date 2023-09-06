package server

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import no.nordicsemi.android.kotlin.ble.core.data.BleGattPermission
import no.nordicsemi.android.kotlin.ble.core.data.BleGattProperty
import no.nordicsemi.android.kotlin.ble.server.main.ServerBleGatt
import no.nordicsemi.android.kotlin.ble.server.main.service.ServerBleGattCharacteristicConfig
import no.nordicsemi.android.kotlin.ble.server.main.service.ServerBleGattDescriptorConfig
import no.nordicsemi.android.kotlin.ble.server.main.service.ServerBleGattServiceConfig
import no.nordicsemi.android.kotlin.ble.server.main.service.ServerBleGattServiceType

@SuppressLint("MissingPermission")
actual class KMMBleServer(private val context: Context) {

    private var server: ServerBleGatt? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    actual suspend fun startServer(services: List<KMMBleServerService>) {

        val config = services.map {
            val characteristics = it.characteristics.map {
                val descritptors = it.descriptors.map {
                    it.uuid
                    ServerBleGattDescriptorConfig(
                        it.uuid,
                        it.permissions.toDomain()
                    )
                }

                ServerBleGattCharacteristicConfig(
                    it.uuid,
                    it.properties.toDomain(),
                    it.permissions.toDomain(),
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

    private fun List<KMMBlePermission>.toDomain(): List<BleGattPermission> {
        return this.map {
            when (it) {
                KMMBlePermission.READ -> BleGattPermission.PERMISSION_READ
                KMMBlePermission.WRITE -> BleGattPermission.PERMISSION_WRITE
            }
        }
    }

    private fun List<KMMCharacteristicProperty>.toDomain(): List<BleGattProperty> {
        return this.map {
            when (it) {
                KMMCharacteristicProperty.READ -> BleGattProperty.PROPERTY_READ
                KMMCharacteristicProperty.WRITE -> BleGattProperty.PROPERTY_WRITE
                KMMCharacteristicProperty.NOTIFY -> BleGattProperty.PROPERTY_NOTIFY
                KMMCharacteristicProperty.INDICATE -> BleGattProperty.PROPERTY_INDICATE
            }
        }
    }
}
