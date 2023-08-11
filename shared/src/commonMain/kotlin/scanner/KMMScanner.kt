package scanner

import kotlinx.coroutines.flow.Flow

expect class KMMScanner {

    fun scan(): Flow<KMMDevice>
}

data class KMMDevice(
    val name: String,
    val address: String
)
