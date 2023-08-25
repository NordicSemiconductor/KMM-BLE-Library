package client

expect class KMMDescriptor {

    suspend fun write(value: ByteArray)

    suspend fun read(): ByteArray
}
