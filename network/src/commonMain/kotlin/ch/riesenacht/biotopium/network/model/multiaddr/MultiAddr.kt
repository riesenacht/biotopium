package ch.riesenacht.biotopium.network.model.multiaddr

import ch.riesenacht.biotopium.network.model.PeerId

/**
 * Represents a multi address.
 *
 * @author Manuel Riesen
 */
data class MultiAddr(
    val family: String,
    val host: String,
    val transport: String,
    val port: Int,
    val application: String,
    val protocol: String,
    val peerId: PeerId
) {
    companion object {

        const val separator = "/"

        /**
         * Creates a multi address from a [String].
         */
        fun fromString(str: String): MultiAddr {
            val components = str.split(separator)
            return MultiAddr(components[0], components[1], components[2], components[3].toInt(), components[4], components[5], PeerId(components[6]))
        }
    }

    override fun toString() = "$family$separator$host$separator$port$separator$application$separator$protocol$separator${peerId.base58}"
}