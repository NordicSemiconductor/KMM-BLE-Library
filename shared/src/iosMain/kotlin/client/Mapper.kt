package client

import platform.CoreBluetooth.CBService

internal fun List<*>.toDomain(): KMMServices {
    val services = this.map { it as CBService }
    return KMMServices(services)
}
