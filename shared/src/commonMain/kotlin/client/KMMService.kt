package client

import com.benasher44.uuid.Uuid

expect class KMMService {

    val uuid: Uuid

    fun findCharacteristic(uuid: Uuid): KMMCharacteristic?
}