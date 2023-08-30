package client

import com.benasher44.uuid.Uuid
import kotlinx.coroutines.flow.Flow

expect class KMMCharacteristic {

    fun findDescriptor(uuid: Uuid): KMMDescriptor?

    suspend fun getNotifications(): Flow<ByteArray>

    suspend fun write(value: ByteArray, writeType: KMMWriteType = KMMWriteType.DEFAULT)

    suspend fun read(): ByteArray
}
