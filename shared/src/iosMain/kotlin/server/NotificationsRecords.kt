package server

import com.benasher44.uuid.Uuid
import platform.CoreBluetooth.CBCentral

class NotificationsRecords {

    private val notifications = mutableMapOf<Uuid, List<CBCentral>>()

    fun addCentral(uuid: Uuid, central: CBCentral) {
        notifications[uuid] = (notifications[uuid] ?: emptyList()) + central
    }

    fun removeCentral(uuid: Uuid, central: CBCentral) {
        notifications[uuid] = (notifications[uuid] ?: emptyList()) - central
    }

    fun getCentrals(uuid: Uuid): List<CBCentral> {
        return notifications[uuid] ?: emptyList()
    }
}
