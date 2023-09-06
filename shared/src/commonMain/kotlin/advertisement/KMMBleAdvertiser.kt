package advertisement

expect class KMMBleAdvertiser {

    suspend fun advertise(settings: KMMAdvertisementSettings)

    suspend fun stop()
}
