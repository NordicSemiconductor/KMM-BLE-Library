package client

import com.benasher44.uuid.Uuid

expect class KMMServices {

    fun findService(uuid: Uuid): KMMService?
}
