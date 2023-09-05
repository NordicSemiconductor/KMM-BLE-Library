package advertisement

import com.benasher44.uuid.Uuid

expect class KMMBleAdvertiser {

    suspend fun advertise(settings: KMMAdvertisementSettings)
}

data class KMMAdvertisementSettings(
    val name: String,
    val uuid: Uuid
)
