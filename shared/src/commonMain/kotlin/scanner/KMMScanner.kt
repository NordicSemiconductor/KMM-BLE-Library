package scanner

import kotlinx.coroutines.flow.Flow

expect class KMMScanner {

    fun scan(): Flow<List<KMMDevice>>
}
