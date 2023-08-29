package client

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.create
import platform.posix.memcpy

/**
 * Converts [NSData] to [ByteArray]
 *
 * @return Converted [ByteArray].
 *
 * @see [StackOverflow](https://stackoverflow.com/a/58521109)
 */
@OptIn(ExperimentalForeignApi::class)
internal fun NSData.toByteArray(): ByteArray = ByteArray(length.toInt()).apply {
    if (length > 0u) {
        usePinned {
            memcpy(it.addressOf(0), bytes, length)
        }
    }
}

/**
 * Converts [ByteArray] to [NSData]
 *
 * @return Converted [NSData].
 *
 * @see [StackOverflow](https://stackoverflow.com/a/58521109)
 */
@OptIn(ExperimentalForeignApi::class)
internal fun ByteArray.toNSData(): NSData = memScoped {
    NSData.create(
        bytes = allocArrayOf(this@toNSData),
        length = size.convert(),
    )
}
