package dev.lbuddyboy.flash.server

import lombok.Data
import lombok.RequiredArgsConstructor
import java.util.*

@RequiredArgsConstructor
@Data
class Server {
    private val uuid: UUID? = null
    private val name: String? = null
    private val motd: String? = null
    private val lastResponse: Long = 0
    private val whitelist = false
    private val online = 0
    private val maxPlayers = 0
    fun status(): String {
        return if (!isOffline()) if (whitelist) "&eWhitelisted" else "&aOnline" else "&cOffline"
    }

    fun isOffline(): Boolean {
        return lastResponse + 15000L < System.currentTimeMillis()
    }
}